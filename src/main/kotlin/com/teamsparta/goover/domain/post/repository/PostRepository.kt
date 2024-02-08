package com.teamsparta.goover.domain.post.repository

import com.teamsparta.goover.domain.post.model.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface PostRepository : JpaRepository<Post, Long> {
    fun findWithLikesById(id: Long): Post?
    fun findByTitle(title: String): List<Post>
    fun findAllByCreatedAtBetween(startDate: LocalDateTime, endDate: LocalDateTime): List<Post>
    fun findAllByOrderByCreatedAtDesc(pageable: Pageable): Page<Post>}


