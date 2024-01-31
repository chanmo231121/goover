package com.teamsparta.goover.domain.user.service

import com.teamsparta.goover.api.user.dto.Request.UserLoginRequest
import com.teamsparta.goover.api.user.dto.Response.UserResponse
import com.teamsparta.goover.api.user.dto.Request.UserSignUpRequest
import com.teamsparta.goover.api.user.dto.Response.LoginResponse

interface UserService {

    fun signUp(request: UserSignUpRequest): UserResponse
    fun login(request: UserLoginRequest):LoginResponse
    fun checkNameExistence(name:String):Boolean



}