package com.community.kalagato.api.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.community.kalagato.KalaGato
import com.community.kalagato.api.ApiService
import com.community.kalagato.api.model.CommentsModel
import com.community.kalagato.database.AppDatabase
import com.community.kalagato.database.FavoritePost
import com.community.kalagato.database.FavoritePostDao
import kotlinx.coroutines.launch

class CommentsViewModel (application: Application) : AndroidViewModel(application) {

    private val favoritePostDao: FavoritePostDao
    val allFavorites: LiveData<List<FavoritePost>>

    init {
        val database = AppDatabase.getDatabase(application)
        favoritePostDao = database.favoritePostDao()
        allFavorites = favoritePostDao.getAllFavoritePosts()
    }

    private val _comments = MutableLiveData<List<CommentsModel>?>()
    val comments: LiveData<List<CommentsModel>?>
        get() = _comments

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun fetchComments(postId:String) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val response = KalaGato.getInstance().getApiService(ApiService::class.java)?.getcomments(postId)
                if (response?.isSuccessful == true) {
                    _comments.value = response.body()
                } else {
                    Log.e("CommentsViewModel", "API Error: ${response?.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("CommentsViewModel", "Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun insertFavorite(post: FavoritePost) = viewModelScope.launch {
        favoritePostDao.insertFavoritePost(post)
    }

    fun deleteFavorite(post: FavoritePost) = viewModelScope.launch {
        favoritePostDao.deleteFavoritePost(post)
    }

    fun isPostInFavorites(postId: Int): LiveData<Boolean> {
        return favoritePostDao.isPostInFavorites(postId)
    }

}