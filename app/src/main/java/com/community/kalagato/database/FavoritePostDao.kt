package com.community.kalagato.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoritePostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoritePost(post: FavoritePost)

    @Delete
    suspend fun deleteFavoritePost(post: FavoritePost)

    @Query("SELECT * FROM favorite_posts")
    fun getAllFavoritePosts(): LiveData<List<FavoritePost>>

    @Query("SELECT EXISTS (SELECT 1 FROM favorite_posts WHERE postId = :postId)")
    fun isPostInFavorites(postId: Int): LiveData<Boolean>
}
