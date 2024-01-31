package com.teamsparta.goover.domain.user.repository

import com.teamsparta.goover.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository :JpaRepository<User,Long> {
    fun existsByName(name: String) : Boolean
    fun findByName(name:String) : User?
}