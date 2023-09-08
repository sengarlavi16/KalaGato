package com.community.kalagato

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.community.kalagato.adapter.CommentsAdapter
import com.community.kalagato.api.viewmodel.CommentsViewModel
import com.community.kalagato.database.FavoritePost
import com.community.kalagato.databinding.ActivityDetailsBinding

class DetailsActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var viewModel: CommentsViewModel
    private var isFav: Boolean = false
    private var postId: Int = 0
    private lateinit var favoritePost: FavoritePost

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_details)

        viewModel = ViewModelProvider(this).get(CommentsViewModel::class.java)

        val postId = intent.getStringExtra("postId")?.toIntOrNull() ?: 0
        val title =  intent.getStringExtra("title").toString()
        binding.txtTitle.text = title
        val body = intent.getStringExtra("body").toString()
        binding.txtBody.text = body

        commentsAdapter = CommentsAdapter()
        binding.recyComments.layoutManager = LinearLayoutManager(this)
        binding.recyComments.adapter = commentsAdapter

        viewModel.comments.observe(this, { comments ->
            commentsAdapter.submitList(comments)
        })

        viewModel.fetchComments(postId.toString())

        viewModel.isPostInFavorites(postId).observe(this, { inFavorites ->
            isFav = inFavorites
            if (isFav) {
                binding.imgfav.setImageDrawable(getDrawable(R.drawable.ic_fav_fill))
            } else {
                binding.imgfav.setImageDrawable(getDrawable(R.drawable.ic_fav_unfill))
            }
        })

        binding.imgback.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })

        binding.imgfav.setOnClickListener(View.OnClickListener {
            if (isFav) {
                binding.imgfav.setImageDrawable(getDrawable(R.drawable.ic_fav_unfill))
                isFav = false
                val favoritePost = FavoritePost(postId, title, body)
                viewModel.deleteFavorite(favoritePost)
            } else {
                binding.imgfav.setImageDrawable(getDrawable(R.drawable.ic_fav_fill))
                isFav = true
                val favoritePost = FavoritePost(postId, title, body)
                viewModel.insertFavorite(favoritePost)
            }
        })

    }

}