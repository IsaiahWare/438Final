package com.example.redditclone.data

data class PostFull(val title: String,
                    val content: String,
                    val author: String,
                    val upvotes: Int,
                    val downvotes: Int,
                    val upvoteUsers: Any,
                    val downvoteUsers: Any,
                    val id: String)