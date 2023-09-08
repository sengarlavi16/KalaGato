package com.community.kalagato.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.community.kalagato.api.model.CommentsModel
import com.community.kalagato.databinding.ChildCommentsBinding

class CommentsAdapter : ListAdapter<CommentsModel, CommentsAdapter.MyViewHolder>(CommentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ChildCommentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class MyViewHolder(private val binding: ChildCommentsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CommentsModel) {
            binding.txtName.text = item.name
            binding.txtEmail.text = item.email
            binding.txtBody.text = item.body
        }
    }

    class CommentDiffCallback : DiffUtil.ItemCallback<CommentsModel>() {
        override fun areItemsTheSame(oldItem: CommentsModel, newItem:CommentsModel): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: CommentsModel, newItem: CommentsModel): Boolean {
            return oldItem == newItem
        }
    }
}