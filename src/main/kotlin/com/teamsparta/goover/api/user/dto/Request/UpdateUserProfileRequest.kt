package com.teamsparta.goover.api.user.dto.Request

import org.springframework.web.multipart.MultipartFile

data class UpdateUserProfileRequest(

    val email:String,
    var profilePic:MutableList<MultipartFile>
){
    fun isPicsEmpty(): Boolean {
        return profilePic?.get(0)?.originalFilename == ""
    }
}
