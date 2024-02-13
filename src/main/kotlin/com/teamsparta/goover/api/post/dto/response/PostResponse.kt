package com.teamsparta.goover.api.post.dto.response

data class PostResponse(
    val id: Long,
    val title: String,
    val content: String,
    var postPicUrl: MutableList<String>
)