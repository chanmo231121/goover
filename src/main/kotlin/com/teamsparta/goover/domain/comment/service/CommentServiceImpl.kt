package com.teamsparta.goover.domain.comment.service

import com.teamsparta.goover.api.comment.dto.Request.CreateCommentRequest
import com.teamsparta.goover.api.comment.dto.Request.UpdateCommentRequest
import com.teamsparta.goover.api.comment.dto.Response.CommentResponse
import com.teamsparta.goover.domain.comment.model.Comment
import com.teamsparta.goover.domain.comment.model.toResponse
import com.teamsparta.goover.domain.comment.repository.CommentRepository
import com.teamsparta.goover.domain.post.repository.PostRepository
import com.teamsparta.goover.domain.user.repository.UserRepository
import com.teamsparta.goover.global.exception.ModelNotFoundException
import com.teamsparta.goover.global.exception.UnauthorizedException
import com.teamsparta.goover.infra.security.UserPrincipal
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
) : CommentService {
    override fun createComment(postId: Long, request: CreateCommentRequest): CommentResponse {
        val post = postRepository.findByIdOrNull(postId) ?: throw ModelNotFoundException("post", postId)
        val userPrincipal = SecurityContextHolder.getContext().authentication.principal as? UserPrincipal
            ?: throw UnauthorizedException("로그인이 필요합니다.")
        val user = userRepository.findById(userPrincipal.id).orElseThrow {
            ModelNotFoundException("user", userPrincipal.id)
        }
        val comment = Comment(
            description = request.description,
            post = post,
            user = user,
        )
        val createdComment = commentRepository.save(comment)
        return createdComment.toResponse()
    }

    override fun deleteComment(userPrincipal: UserPrincipal, commentId: Long, postId: Long): String {
        val post = postRepository.findByIdOrNull(postId) ?: throw ModelNotFoundException("post", postId)
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException("comment", commentId)
        if (comment.post.id != post.id) {
            throw ModelNotFoundException("comment", commentId)
        }
        if (comment.user.id != userPrincipal.id) {
            throw UnauthorizedException("다른 사람의 댓글을 삭제 할 수없습니다.")
        }
        commentRepository.delete(comment)
        return "댓글이 성공적으로 삭제되었습니다."

    }

    override fun updateComment(postId: Long, commentId: Long, updateRequest: UpdateCommentRequest): CommentResponse {
        val userPrincipal = SecurityContextHolder.getContext().authentication.principal as? UserPrincipal
            ?: throw UnauthorizedException("로그인이 필요합니다.")
        val comment = commentRepository.findByPostIdAndId(postId, commentId)
                ?: throw ModelNotFoundException("comment", commentId)
        if (comment.user.id != userPrincipal.id) {
            throw UnauthorizedException("다른 사람의 댓글을 수정 할 수없습니다.")
        }
        comment.description = updateRequest.description
        val updatedComment = commentRepository.save(comment)
        return updatedComment.toResponse()

    }

    override fun getComment(postId: Long, commentId: Long): CommentResponse {
        val userPrincipal = SecurityContextHolder.getContext().authentication.principal as? UserPrincipal
            ?: throw UnauthorizedException("로그인이 필요합니다.")
        val comment = commentRepository.findByPostIdAndIdAndUserId(postId,commentId,userPrincipal.id)
            ?: throw UnauthorizedException("내가 작성한 댓글만 볼수있습니다.")
        return comment.toResponse()
    }

    override fun getAllComment(): List<CommentResponse> {
        val userPrincipal = SecurityContextHolder.getContext().authentication.principal as? UserPrincipal
            ?: throw UnauthorizedException("로그인이 필요합니다.")
        val comment = commentRepository.findByUserId(userPrincipal.id)
        return comment.map { it.toResponse() }
    }

}