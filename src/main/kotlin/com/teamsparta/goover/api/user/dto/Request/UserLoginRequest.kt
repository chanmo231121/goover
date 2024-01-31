package com.teamsparta.goover.api.user.dto.Request

data class UserLoginRequest(
    val name: String,
    val password:String,
    val role:String,
)

