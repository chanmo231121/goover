package com.teamsparta.goover.api.user.dto.Request

import com.teamsparta.goover.domain.user.model.Role
import com.teamsparta.goover.domain.user.model.User

data class UserSignUpRequest(

    var name:String,
    var email:String,
    var password:String,
    var confirmpassword:String,
    var role:String,
){
    fun to():User{

       if (!name.matches(Regex("^[a-zA-Z0-9]{3,}$"))){
            throw IllegalArgumentException("닉네임은 최소3글자이상,알파벳 대소문자,숫자0~9로 이루워져있습니다")
        }
        if (password.length < 4 || password.contains(name)){
            throw IllegalArgumentException("비밀번호는 최소 4자 이상이며, 닉네임과 같은 값이 포함될 수없습니다.")
        }
        if (password != confirmpassword){
            throw IllegalArgumentException("비밀번호와 확인 비밀번호가 일치하지 않습니다")
        }

        val memberRole = when (role) {
            Role.USER.name -> Role.USER
            Role.ADMIN.name -> Role.ADMIN
            else -> throw IllegalArgumentException("권한을 제대로 입력해주세요.")
        }
        return User(
            name = name,
            email = email,
            password = password,
            role = memberRole
        )
    }
}
