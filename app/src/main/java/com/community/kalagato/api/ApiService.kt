package com.community.kalagato.api

import com.community.kalagato.api.model.CommentsModel
import com.community.kalagato.api.model.PostsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    /*Get Posts Api*/
    @GET("posts")
    suspend fun getposts(): Response<List<PostsModel>>

    /*Get Comments Api*/
    @GET("posts/{post_id}/comments")
    suspend fun getcomments(@Path("post_id") postId: String): Response<List<CommentsModel>>

}