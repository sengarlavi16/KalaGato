package com.community.kalagato.api.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.community.kalagato.PostsViewModel
import com.community.kalagato.database.AppDatabase

class PostsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostsViewModel::class.java)) {
            val database = AppDatabase.getDatabase(application)
            return PostsViewModel(application,database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}