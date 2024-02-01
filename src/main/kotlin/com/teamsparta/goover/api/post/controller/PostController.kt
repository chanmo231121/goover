package com.teamsparta.goover.api.post.controller

import com.teamsparta.goover.api.post.dto.request.PostCreateRequest
import com.teamsparta.goover.api.post.dto.request.PostUpdateRequest
import com.teamsparta.goover.api.post.dto.response.PostResponse
import com.teamsparta.goover.domain.post.service.PostService
import com.teamsparta.goover.infra.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/post")
class PostController(
    private val postService: PostService
) {

    @PostMapping
    fun createPost(
        @RequestBody createRequest: PostCreateRequest
    ): ResponseEntity<PostResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.create(createRequest))
    }

    @DeleteMapping("{postId}")
    fun deletePost(
        @PathVariable postId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<String> {
        val message = postService.delete(userPrincipal, postId)
        return ResponseEntity
            .ok()
            .body(message)
    }

    @PutMapping("/{postId}")
    fun updatePost(
        @PathVariable postId: Long,
        @RequestBody postUpdateRequest: PostUpdateRequest
    ): ResponseEntity<PostResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.update(postId,postUpdateRequest))
    }
        @GetMapping("/{postId}")
        fun getPost(@PathVariable postId: Long):ResponseEntity<PostResponse>{
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(postService.get(postId))
        }
        @GetMapping
        fun getAllPost():ResponseEntity<List<PostResponse>>{
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(postService.getAll())
        }


}