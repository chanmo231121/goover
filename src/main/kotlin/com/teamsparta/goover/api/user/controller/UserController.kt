package com.teamsparta.goover.api.user.controller

import com.teamsparta.goover.api.user.dto.Request.UserLoginRequest
import com.teamsparta.goover.api.user.dto.Request.UserSignUpRequest
import com.teamsparta.goover.api.user.dto.Response.LoginResponse
import com.teamsparta.goover.api.user.dto.Response.UserResponse
import com.teamsparta.goover.domain.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/app-users")
class UserController (
   private val userService: UserService
){


    @PostMapping("/signup")
    fun signup( @RequestBody signUpRequest: UserSignUpRequest): ResponseEntity<UserResponse>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.signUp(signUpRequest))
    }

    @GetMapping("/check-name/{name}")
    fun checkname(@PathVariable name: String):ResponseEntity<String>{
        return if (userService.checkNameExistence(name)){
            ResponseEntity.badRequest().body("닉네임이 이미 존재합니다.")
        } else {
            ResponseEntity.ok("사용가능한 닉네임입니다.")
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: UserLoginRequest):ResponseEntity<LoginResponse>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.login(loginRequest))
    }



}