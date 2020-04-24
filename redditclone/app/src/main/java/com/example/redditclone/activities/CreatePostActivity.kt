package com.example.redditclone.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.redditclone.R
import com.example.redditclone.data.Post
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.create_post_layout.*
import kotlinx.android.synthetic.main.home_layout.*

class CreatePostActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var username = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_post_layout)

        auth = FirebaseAuth.getInstance()

        // create an instance of the firebase database
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.firestoreSettings = settings

        createPostSubmit.setOnClickListener {
            createPost()
        }
    }

    public override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser

        db.collection("users").get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        if (document.get("email") == currentUser?.email) {
                            username = document.get("username").toString()
                            Log.d("username", username)
                            break
                        }
                    }
                }
            })
    }

    private fun createPost() {
        val title = createPostTitle.text.toString()
        val content = createPostContent.text.toString()
        if (title.equals("") || content.equals("")) {
            return
        }
        val post = Post(title, content, username,0,0, ArrayList<String>(), ArrayList<String>())
        db.collection("posts")
            .add(post)
            .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                Log.d("post", "post created")
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            })
            .addOnFailureListener(OnFailureListener { e ->
                Toast.makeText(this, "Failed to insert data!", Toast.LENGTH_LONG)
            })
    }

}