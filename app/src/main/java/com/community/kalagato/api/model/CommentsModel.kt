package com.community.kalagato.api.model

import com.google.gson.annotations.SerializedName

data class CommentsModel (

    @SerializedName("name")
    val name: String,

    @SerializedName("postId")
    val postId: Int,

    @SerializedName("id")
    val id: Int,

    @SerializedName("body")
    val body: String,

    @SerializedName("email")
    val email: String
)
