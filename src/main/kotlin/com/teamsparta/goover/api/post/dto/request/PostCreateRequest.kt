package com.teamsparta.goover.api.post.dto.request

import jakarta.validation.constraints.Size
import org.springframework.web.multipart.MultipartFile

data class PostCreateRequest(

    @field:Size(max = 500, message = "제목은 최대 500자까지 입력 가능합니다.")
    val title: String,

    @field:Size(max = 5000, message = "작성 내용은 최대 5000자까지 입력 가능합니다.")
    val content: String,

    var postPic:MutableList<MultipartFile>

){
    fun isPicsEmpty(): Boolean {
        return postPic?.get(0)?.originalFilename == ""
    }
}