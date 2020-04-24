package com.example.redditclone.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.redditclone.R
import com.example.redditclone.adapter.CommentListAdapter
import com.example.redditclone.adapter.PostListAdapter
import com.example.redditclone.data.Comment
import com.example.redditclone.data.Post
import com.example.redditclone.data.PostFull
import com.example.redditclone.data.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.expanded_post_layout.*
import kotlinx.android.synthetic.main.home_layout.*

import kotlinx.android.synthetic.main.signin_layout.*

class ExpandedPostActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    var id = ""
    var title = ""
    var content = ""
    var author = ""
    var upvotes = 0
    var downvotes = 0
    var upvoteUsers = ArrayList<String>()
    var downvoteUsers = ArrayList<String>()
    var commentList = ArrayList<Comment>()
    var username = ""
    lateinit var adapter: CommentListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.expanded_post_layout)

        auth = FirebaseAuth.getInstance()

        // create an instance of the firebase database
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.firestoreSettings = settings

        val recyclerView = findViewById<RecyclerView>(R.id.comment_list_layout)
        adapter = CommentListAdapter(commentList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        addCommentButton.setOnClickListener {
            addComment()
        }
        downvoteButton.setOnClickListener {
            downvote(it)
        }
        upvoteButton.setOnClickListener {
            upvote(it)
        }
    }

    public override fun onStart() {
        super.onStart()

        id = intent.getStringExtra("id")

        commentList.clear()

        refreshPost()
        refreshComments()
        getUsername()
    }

    fun addComment() {
        var content = commentContent.text.toString()
        var author = username
        if (content.equals("")) {
            return
        }
        var comment = Comment(id, author, content, 0, 0, ArrayList<String>(), ArrayList<String>())
        db.collection("comments")
            .add(comment)
            .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                refreshComments()
            })
    }

    fun upvote(v: View) {
        val userID = auth.currentUser?.uid
        for (user in upvoteUsers) {
            if (user.equals(userID)) {
                return
            }
        }
        for (user in downvoteUsers) {
            if (user.equals(userID)) {
                downvoteUsers.remove(user)
                downvotes--
                break
            }
        }
        upvoteUsers.add(userID!!)
        upvotes++
        var post = Post(title, content, author, upvotes, downvotes, upvoteUsers, downvoteUsers)
        db.collection("posts").document(id).set(post).addOnCompleteListener(OnCompleteListener {
            refreshPost()
        })
    }

    fun downvote(v: View) {
        val userID = auth.currentUser?.uid
        for (user in downvoteUsers) {
            if (user.equals(userID)) {
                return
            }
        }
        for (user in upvoteUsers) {
            if (user.equals(userID)) {
                upvoteUsers.remove(user)
                upvotes--
                break
            }
        }
        downvoteUsers.add(userID!!)
        downvotes++
        var post = Post(title, content, author, upvotes, downvotes, upvoteUsers, downvoteUsers)
        db.collection("posts").document(id).set(post).addOnCompleteListener(OnCompleteListener {
            refreshPost()
        })
    }

    fun refreshPost() {
        db.collection("posts").get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        if (document.id == id) {
                            title = document.get("title").toString()
                            content = document.get("content").toString()
                            author = document.get("author").toString()
                            upvotes = document.get("upvotes").toString().toInt()
                            downvotes = document.get("downvotes").toString().toInt()
                            upvoteUsers = document.get("upvoteUsers") as ArrayList<String>
                            downvoteUsers = document.get("downvoteUsers") as ArrayList<String>
                            expandedPostTitle.text = title
                            expandedPostAuthor.text = author
                            expandedPostContent.text = content
                            expandedPostUpvotes.text = upvotes.toString()
                            expandedPostDownvotes.text = downvotes.toString()
                            break
                        }
                    }
                } else {
                    println("failed to get data")
                }
            })
    }

    fun refreshComments() {
        db.collection("comments").get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        if (document.get("post_id") == id) {
                            content = document.get("content").toString()
                            author = document.get("author").toString()
                            upvotes = document.get("upvotes").toString().toInt()
                            downvotes = document.get("downvotes").toString().toInt()
                            upvoteUsers = document.get("upvoteUsers") as ArrayList<String>
                            downvoteUsers = document.get("downvoteUsers") as ArrayList<String>
                            val comment = Comment(id, author, content, upvotes, downvotes, upvoteUsers, downvoteUsers)
                            commentList.add(comment)
                            adapter.notifyDataSetChanged()
                            break
                        }
                    }
                } else {
                    println("failed to get data")
                }
            })
    }

    fun getUsername() {
        db.collection("users").get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        if (document.get("email") == auth.currentUser?.email) {
                            username = document.get("username").toString()
                            break
                        }
                    }
                }
            })
    }
}
