package com.example.redditclone.data

data class Comment(val post_id: String,
                   val author: String,
                   val content: String,
                   val upvotes: Int,
                   val downvotes: Int,
                   val upvoteUsers: ArrayList<String>,
                   val downvoteUsers:ArrayList<String>)