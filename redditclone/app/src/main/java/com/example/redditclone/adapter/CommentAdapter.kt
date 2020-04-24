package com.example.redditclone.adapter


import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.redditclone.R
import com.example.redditclone.activities.CreatePostActivity
import com.example.redditclone.activities.ExpandedPostActivity
import com.example.redditclone.data.Comment
import com.example.redditclone.data.Post
import com.example.redditclone.data.PostFull


//define the binding for the view holder
class CommentListViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.single_comment_layout, parent, false)) {
    private val content: TextView
    private val author: TextView

    init {
        content = itemView.findViewById(R.id.commentContent)
        author = itemView.findViewById(R.id.commentAuthor)
    }

    fun bind(comment: Comment) {
        content.text = comment.content
        author.text = comment.author
    }
}

//define the adapter for the recycler view
class CommentListAdapter(private val list: ArrayList<Comment>)
    : RecyclerView.Adapter<CommentListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CommentListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: CommentListViewHolder, position: Int) {
        val c: Comment = list[position]
        holder.bind(c)
    }

    override fun getItemCount(): Int = list.size

}