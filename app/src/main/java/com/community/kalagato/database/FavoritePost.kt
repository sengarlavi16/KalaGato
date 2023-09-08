package com.community.kalagato.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_posts")
data class FavoritePost(
    @PrimaryKey val postId: Int,
    val title: String,
    val body: String
)