package com.community.kalagato.api.model

import com.google.gson.annotations.SerializedName

data class PostsModel(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("body")
    val body: String,

    @SerializedName("userId")
    val userId: Int
)