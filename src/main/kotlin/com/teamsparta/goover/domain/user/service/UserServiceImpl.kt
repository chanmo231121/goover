package com.teamsparta.goover.domain.user.service

import com.teamsparta.goover.api.user.dto.Request.UserLoginRequest
import com.teamsparta.goover.api.user.dto.Request.UserSignUpRequest
import com.teamsparta.goover.api.user.dto.Response.LoginResponse
import com.teamsparta.goover.api.user.dto.Response.UserResponse
import com.teamsparta.goover.domain.user.repository.UserRepository
import com.teamsparta.goover.infra.security.jwt.JwtPlugin
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl(

    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val emailService: EmailService,
    private val jwtPlugin: JwtPlugin,
) : UserService {

    @Transactional
    override fun login(request: UserLoginRequest): LoginResponse {
        val user = userRepository.findByName(request.name)  ?:
        throw IllegalArgumentException("닉네임 또는 비밀번호를 확인해주세요.")
        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("닉네임 또는 비밀번호를 확인해주세요.")
        }
        return LoginResponse(
            accessToken = jwtPlugin.generateAccessToken(
                subject = user.id.toString(),
                name = user.name,
                role = user.role.name,
            ),
            name = user.name
        )
    }
    @Transactional
    override fun signUp(request: UserSignUpRequest): UserResponse {
        if (userRepository.existsByName(request.name)) {
            throw IllegalArgumentException("닉네임이 존재합니다")
        }
        val hashedPassword = passwordEncoder.encode(request.password)
        val user = userRepository.save(request.to().apply { password = hashedPassword })
        val verificationCode = generateVerificationCode()
        emailService.sendVerificationEmail(user.email, verificationCode)
        return UserResponse.from(user)
    }
    private fun generateVerificationCode(): String {
        return UUID.randomUUID().toString().substring(0, 6)
    }
    @Transactional
    override fun checkNameExistence(name: String): Boolean {
        return userRepository.existsByName(name)
    }


}