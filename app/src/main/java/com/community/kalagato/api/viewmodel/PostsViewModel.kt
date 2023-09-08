package com.community.kalagato

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.community.kalagato.api.ApiService
import com.community.kalagato.api.model.PostsModel
import com.community.kalagato.database.AppDatabase
import com.community.kalagato.database.FavoritePost
import com.community.kalagato.database.FavoritePostDao
import kotlinx.coroutines.launch

class PostsViewModel (application: Application, database: AppDatabase) : AndroidViewModel(application) {
    private val favoritePostDao: FavoritePostDao
    val allFavorites: LiveData<List<FavoritePost>>

    init {
        val database = AppDatabase.getDatabase(application)
        favoritePostDao = database.favoritePostDao()
        allFavorites = favoritePostDao.getAllFavoritePosts()
    }

    private val _posts = MutableLiveData<List<PostsModel>?>()
    val posts: LiveData<List<PostsModel>?>
        get() = _posts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun fetchPosts() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val response = KalaGato.getInstance().getApiService(ApiService::class.java)?.getposts()
                if (response?.isSuccessful == true) {
                    _posts.value = response.body()
                } else {
                    Log.e("PostsViewModel", "API Error: ${response?.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("PostsViewModel", "Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getAllFavoritePosts(): LiveData<List<FavoritePost>> {
        return allFavorites
    }

}