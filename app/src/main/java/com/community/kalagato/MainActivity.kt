package com.community.kalagato

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.community.kalagato.adapter.FavPostAdapter
import com.community.kalagato.adapter.PostsAdapter
import com.community.kalagato.api.model.PostsModel
import com.community.kalagato.api.viewmodel.PostsViewModelFactory
import com.community.kalagato.database.AppDatabase
import com.community.kalagato.database.FavoritePost
import com.community.kalagato.databinding.ActivityMainBinding
import com.community.kalagato.utils.NetworkCheck
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), PostsAdapter.OnPostClickListener,
    FavPostAdapter.OnPostClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var postsAdapter: PostsAdapter
    private lateinit var favPostAdapter: FavPostAdapter
    private lateinit var viewModel: PostsViewModel
    private lateinit var networkChangeReceiver: NetworkChangeReceiver
    private var isNetworkAvailable = true
    private var isPostsLoadedFromNetwork = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main)

        AppDatabase.getDatabase(this)

        viewModel = ViewModelProvider(this, PostsViewModelFactory(application)).get(PostsViewModel::class.java)

        postsAdapter = PostsAdapter(this)
        binding.recyPosts.layoutManager = LinearLayoutManager(this)
        binding.recyPosts.adapter = postsAdapter

        favPostAdapter = FavPostAdapter(this)
        binding.recyFavourites.layoutManager = LinearLayoutManager(this)
        binding.recyFavourites.adapter = favPostAdapter

        binding.txtPost.setOnClickListener(View.OnClickListener {
            ChangeTab(binding.postsview ,binding.recyPosts)
        })

        binding.txtFavourites.setOnClickListener(View.OnClickListener {
            ChangeTab(binding.favview ,binding.recyFavourites)
        })

        viewModel.posts.observe(this, { posts ->
            postsAdapter.submitList(posts)
            if (posts != null) {
                savePostsToSharedPreferences(posts)
            }
        })

        val cachedPosts = loadCachedPosts()
        if (cachedPosts.isNotEmpty()) {
            isPostsLoadedFromNetwork = true
            postsAdapter.submitList(cachedPosts)
        }

        networkChangeReceiver = NetworkChangeReceiver()
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, intentFilter)

        if (NetworkCheck.isConnected(this)) {
            viewModel.fetchPosts()
        } else {
            isNetworkAvailable = false
            val snackBar = Snackbar.make(
                binding.root,
                "No internet connection",
                Snackbar.LENGTH_LONG
            )
            snackBar.setBackgroundTint(
                ContextCompat.getColor(this, R.color.grey)
            )
            snackBar.show()
        }

        viewModel.getAllFavoritePosts().observe(this, { favoritePosts ->
            favPostAdapter.submitList(favoritePosts)
        })

    }

    fun ChangeTab(view: View, recyclerView: RecyclerView) {
        binding.recyPosts.visibility = View.GONE
        binding.recyFavourites.visibility = View.GONE

        binding.postsview.visibility = View.GONE
        binding.favview.visibility = View.GONE

        recyclerView.visibility = View.VISIBLE
        view.visibility = View.VISIBLE
    }

    override fun onPostClick(item: PostsModel) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("postId", item.id.toString())
        intent.putExtra("title", item.title)
        intent.putExtra("body", item.body)
        startActivity(intent)
    }

    override fun onPostClick(item: FavoritePost) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("postId", item.postId.toString())
        intent.putExtra("title", item.title)
        intent.putExtra("body", item.body)
        startActivity(intent)
    }

    inner class NetworkChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (NetworkCheck.isConnected(this@MainActivity)) {
                viewModel.fetchPosts()
                if (!isNetworkAvailable) {
                    isNetworkAvailable = true
                    val snackBar = Snackbar.make(
                        binding.root,
                        "Internet is Back",
                        Snackbar.LENGTH_LONG
                    )
                    snackBar.setBackgroundTint(
                        ContextCompat.getColor(this@MainActivity, R.color.grey)
                    )
                    snackBar.show()
                }
            } else {
                isNetworkAvailable = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isNetworkAvailable && NetworkCheck.isConnected(this)) {
            isNetworkAvailable = true
            viewModel.fetchPosts()
            val snackBar = Snackbar.make(
                binding.root,
                "Internet is back",
                Snackbar.LENGTH_LONG
            )
            snackBar.setBackgroundTint(
                ContextCompat.getColor(this, R.color.grey)
            )
            snackBar.show()
        }
    }

    private fun savePostsToSharedPreferences(posts: List<PostsModel>) {
        val gson = Gson()
        val json = gson.toJson(posts)
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("posts", json)
        editor.apply()
    }

    private fun loadCachedPosts(): List<PostsModel> {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("posts", "")
        if (json.isNullOrEmpty()) {
            return emptyList()
        }
        val gson = Gson()
        return gson.fromJson(json, Array<PostsModel>::class.java).toList()
    }

}