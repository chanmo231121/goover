package com.teamsparta.goover.domain.comment.service

import com.teamsparta.goover.api.comment.dto.Request.CreateCommentRequest
import com.teamsparta.goover.api.comment.dto.Request.UpdateCommentRequest
import com.teamsparta.goover.api.comment.dto.Response.CommentResponse
import com.teamsparta.goover.infra.security.UserPrincipal

interface CommentService {

    fun createComment(postId: Long,request: CreateCommentRequest): CommentResponse
    fun deleteComment(userPrincipal: UserPrincipal, commentId: Long, postId: Long):String
    fun updateComment(postId:Long, commentId: Long, updateRequest: UpdateCommentRequest): CommentResponse
    fun getComment(postId: Long,commentId: Long):CommentResponse
    fun getAllComment():List<CommentResponse>
}