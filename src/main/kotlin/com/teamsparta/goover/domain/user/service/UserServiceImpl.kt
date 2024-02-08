package com.teamsparta.goover.domain.user.service

import com.teamsparta.goover.api.user.dto.Request.UpdateUserProfileRequest
import com.teamsparta.goover.api.user.dto.Request.UserLoginRequest
import com.teamsparta.goover.api.user.dto.Request.UserSignUpRequest
import com.teamsparta.goover.api.user.dto.Response.LoginResponse
import com.teamsparta.goover.api.user.dto.Response.UserResponse
import com.teamsparta.goover.domain.user.repository.UserRepository
import com.teamsparta.goover.global.exception.ModelNotFoundException
import com.teamsparta.goover.infra.aws.S3Service
import com.teamsparta.goover.infra.security.UserPrincipal
import com.teamsparta.goover.infra.security.jwt.JwtPlugin
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl(

    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val emailService: EmailService,
    private val jwtPlugin: JwtPlugin,
    private val s3Service: S3Service
) : UserService {

    @Transactional
    override fun login(request: UserLoginRequest): LoginResponse {
        val user = userRepository.findByName(request.name) ?: throw IllegalArgumentException("닉네임 또는 비밀번호를 확인해주세요.")
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
        var uploadedImageStrings: MutableList<String>? = null
        if (!request.isPicsEmpty()) {
            uploadedImageStrings = s3Service.upload(request.profilePic!!, "profile").toMutableList()
        }
        val hashedPassword = passwordEncoder.encode(request.password)
        val user = userRepository.save(request.to().apply {
            password = hashedPassword
            if (uploadedImageStrings != null) {
                profilePicUrl = uploadedImageStrings
            }

        })
        val verificationCode = UUID.randomUUID().toString().substring(0, 6)
        emailService.sendVerificationEmail(user.email, verificationCode)
        return UserResponse.from(user)
    }

    @Transactional
    override fun checkNameExistence(name: String): Boolean {
        return userRepository.existsByName(name)
    }

    override fun updateUserProfile(userId: Long, request: UpdateUserProfileRequest): UserResponse {
        val principal = SecurityContextHolder.getContext().authentication.principal

        val authenticatedId: Long = when (principal) {
            is UserPrincipal -> principal.id
            is String -> principal.toLongOrNull() ?: throw IllegalArgumentException("Invalid authenticated user ID format")
            else -> throw IllegalArgumentException("Unexpected authenticated user type: ${principal.javaClass}")
        }

        if (userId != authenticatedId) {
            throw IllegalArgumentException("프로필 수정 권한이 없습니다")
        }

        val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("user", userId)
        user.email = request.email

        if (request.profilePic != null && !request.isPicsEmpty()) {
            val uploadedImageStrings = s3Service.upload(request.profilePic!!, "profile").toMutableList()
            user.profilePicUrl = uploadedImageStrings
        }

        val updateUser = userRepository.save(user)
        return UserResponse.from(user)
    }
}