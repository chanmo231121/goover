package com.teamsparta.goover.api.comment.controller

import com.teamsparta.goover.api.comment.dto.Request.CreateCommentRequest
import com.teamsparta.goover.api.comment.dto.Request.UpdateCommentRequest
import com.teamsparta.goover.api.comment.dto.Response.CommentResponse
import com.teamsparta.goover.domain.comment.service.CommentService
import com.teamsparta.goover.infra.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/post/{postId}/comment")
class CommentController(
    private val commentService: CommentService
) {

    @PostMapping

    fun createComment(
        @PathVariable postId: Long,
        @RequestBody createRequest: CreateCommentRequest,
    ): ResponseEntity<CommentResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(commentService.createComment(postId,createRequest))
    }

    @DeleteMapping("{commentId}")
    fun deleteComment(
        @PathVariable postId: Long,
        @PathVariable commentId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ResponseEntity<String> {
        val message = commentService.deleteComment(userPrincipal, commentId,postId)
        return ResponseEntity
            .ok()
            .body(message)
    }

    @PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable postId: Long,
        @PathVariable commentId: Long,
        @RequestBody updateCommentRequest: UpdateCommentRequest
    ): ResponseEntity<CommentResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(commentService.updateComment(postId, commentId, updateCommentRequest))
    }

    @GetMapping("/{commentId}")
    fun getComment(
        @PathVariable postId: Long,
        @PathVariable commentId: Long
    ): ResponseEntity<CommentResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(commentService.getComment(postId, commentId))
    }

    @GetMapping
    fun getAllComment(@PathVariable postId: Long): ResponseEntity<List<CommentResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(commentService.getAllComment())
    }
}