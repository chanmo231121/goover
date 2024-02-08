package com.teamsparta.goover.domain.user.service

import com.teamsparta.goover.api.user.dto.Request.UpdateUserProfileRequest
import com.teamsparta.goover.api.user.dto.Request.UserLoginRequest
import com.teamsparta.goover.api.user.dto.Request.UserSignUpRequest
import com.teamsparta.goover.api.user.dto.Response.LoginResponse
import com.teamsparta.goover.api.user.dto.Response.UserResponse

interface UserService {

    fun signUp(request: UserSignUpRequest): UserResponse
    fun login(request: UserLoginRequest):LoginResponse
    fun checkNameExistence(name:String):Boolean

    fun updateUserProfile(userId:Long, request: UpdateUserProfileRequest):UserResponse


}