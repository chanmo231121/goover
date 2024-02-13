package com.teamsparta.goover.domain.post.repository

import com.teamsparta.goover.domain.post.model.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.time.LocalDateTime

interface PostRepository : JpaRepository<Post, Long> {
    fun findWithLikesById(id: Long): Post?
    fun findByTitle(title: String): List<Post>
    fun findAllByCreatedAtBetween(startDate: LocalDateTime, endDate: LocalDateTime): List<Post>
    fun findAllByOrderByCreatedAtDesc(pageable: Pageable): Page<Post>

    @Query("SELECT p FROM Post p WHERE p.createdAt < :thresholdDate")
    fun findOldPosts(@Param("thresholdDate") thresholdDate: LocalDate): List<Post>

   // fun deleteByCreatedDateBefore(date: LocalDate)
}


