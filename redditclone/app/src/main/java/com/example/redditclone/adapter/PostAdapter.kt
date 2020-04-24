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
import com.example.redditclone.data.Post
import com.example.redditclone.data.PostFull


//define the binding for the view holder
class PostListViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.post_preview_layout, parent, false)) {
    private val title: TextView
    private val content: TextView
    private val author: TextView
    private val upvotes: TextView
    private val downvotes: TextView
    private val postLayout: LinearLayout

    init {
        title = itemView.findViewById(R.id.postTitle)
        content = itemView.findViewById(R.id.postContent)
        author = itemView.findViewById(R.id.postAuthor)
        upvotes = itemView.findViewById(R.id.postUpvotes)
        downvotes = itemView.findViewById(R.id.postDownvotes)
        postLayout = itemView.findViewById(R.id.postLayout)
    }

    fun bind(post: PostFull) {
        title.text = post.title
        content.text = post.content
        author.text = post.author
        upvotes.text = post.upvotes.toString()
        downvotes.text = post.downvotes.toString()
        postLayout.setTag(R.string.post_id, post.id)
        postLayout.setOnClickListener {
            val intent = Intent(it.context, ExpandedPostActivity::class.java)
            intent.putExtra("id", post.id)
            it.context.startActivity(intent)
        }
    }
}

//define the adapter for the recycler view
class PostListAdapter(private val list: ArrayList<PostFull>)
    : RecyclerView.Adapter<PostListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PostListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PostListViewHolder, position: Int) {
        val p: PostFull = list[position]
        holder.bind(p)
    }

    override fun getItemCount(): Int = list.size

}