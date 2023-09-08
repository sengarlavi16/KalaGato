package com.community.kalagato.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.community.kalagato.api.model.PostsModel
import com.community.kalagato.databinding.ChildPostsBinding

class PostsAdapter(private val postClickListener: OnPostClickListener) :
    ListAdapter<PostsModel, PostsAdapter.MyViewHolder>(PostDiffCallback()) {

    interface OnPostClickListener {
        fun onPostClick(item: PostsModel)
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
        fun bind(item: PostsModel) {
            binding.txtTitle.text = item.title
            binding.txtBody.text = item.body
        }
    }

    class PostDiffCallback : DiffUtil.ItemCallback<PostsModel>() {
        override fun areItemsTheSame(oldItem: PostsModel, newItem:PostsModel): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: PostsModel, newItem: PostsModel): Boolean {
            return oldItem == newItem
        }
    }
}