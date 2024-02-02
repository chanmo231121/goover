package com.teamsparta.goover.domain.comment.repository

import com.teamsparta.goover.domain.comment.model.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository: JpaRepository<Comment, Long> {

    fun findByPostIdAndId(postId: Long , commentId: Long): Comment?
    fun findByPostIdAndIdAndUserId(postId: Long, commentId: Long, userId: Long): Comment?
    fun findByUserId(userId: Long): List<Comment>
}