package com.community.kalagato.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.community.kalagato.database.FavoritePost
import com.community.kalagato.databinding.ChildPostsBinding

class FavPostAdapter(private val postClickListener: OnPostClickListener) :
    ListAdapter<FavoritePost, FavPostAdapter.MyViewHolder>(PostDiffCallback()) {

    interface OnPostClickListener {
        fun onPostClick(item: FavoritePost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ChildPostsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            postClickListener.onPostClick(item)
        }
    }

    class MyViewHolder(private val binding: ChildPostsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FavoritePost) {
            binding.txtTitle.text = item.title
            binding.txtBody.text = item.body
        }
    }

    class PostDiffCallback : DiffUtil.ItemCallback<FavoritePost>() {
        override fun areItemsTheSame(oldItem: FavoritePost, newItem: FavoritePost): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: FavoritePost, newItem: FavoritePost): Boolean {
            return oldItem == newItem
        }
    }
}