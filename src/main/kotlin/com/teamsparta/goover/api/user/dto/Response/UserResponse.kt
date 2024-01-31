package com.teamsparta.goover.api.user.dto.Response

import com.teamsparta.goover.domain.user.model.User

data class UserResponse(
    var id:Long,
    var name:String,
    var email:String,
    var role:String,
){
    companion object{
        fun from(user: User): UserResponse {
            return UserResponse(
                id = user.id!!,
                email = user.email,
                name = user.name,
                role = user.role.name
            )
        }
    }
}
