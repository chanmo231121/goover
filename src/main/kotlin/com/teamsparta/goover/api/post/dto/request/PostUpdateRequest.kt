package com.teamsparta.goover.api.post.dto.request

import org.springframework.web.multipart.MultipartFile

data class PostUpdateRequest(
    val title: String,
    val content: String,
    var postPic:MutableList<MultipartFile>
    ){
    fun isPicsEmpty(): Boolean {
        return postPic?.get(0)?.originalFilename == ""
    }
}
