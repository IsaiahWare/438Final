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
import com.example.redditclone.adapter.PostListAdapter
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
import kotlinx.android.synthetic.main.home_layout.*

import kotlinx.android.synthetic.main.signin_layout.*

class HomeActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    var postList: ArrayList<PostFull> =  ArrayList()
    var postMap = mutableMapOf<String, PostFull>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_layout)

        auth = FirebaseAuth.getInstance()

        // create an instance of the firebase database
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build()
        db.firestoreSettings = settings

        createPostButton.setOnClickListener {
            goToCreatePost()
        }
    }

    public override fun onStart() {
        super.onStart()
        val recyclerView = findViewById<RecyclerView>(R.id.post_list_layout)
        val adapter = PostListAdapter(postList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        postList.clear()

        // Add posts
        db.collection("posts").get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val id = document.id
                        val title = document.get("title").toString()
                        val content = document.get("content").toString()
                        val author = document.get("author").toString()
                        val upvotes = document.get("upvotes").toString().toInt()
                        val downvotes = document.get("downvotes").toString().toInt()
                        val upvoteUsers = document.get("upvoteUsers")
                        val downvoteUsers = document.get("downvoteUsers")
                        val post = PostFull(title, author, content, upvotes, downvotes, upvoteUsers!!, downvoteUsers!!, id)

                        postList.add(post)
                        postMap.put(id, post)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    println("failed to get data")
                }
            })
            .addOnFailureListener(OnFailureListener { e ->
                Toast.makeText(this, "Failed to get data!", Toast.LENGTH_LONG)
            })

        
    }

    fun goToCreatePost() {
        val intent = Intent(this, CreatePostActivity::class.java)
        startActivity(intent)
    }
}
