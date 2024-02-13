package com.teamsparta.goover.api.post.controller

import com.teamsparta.goover.api.post.dto.request.PostCreateRequest
import com.teamsparta.goover.api.post.dto.request.PostUpdateRequest
import com.teamsparta.goover.api.post.dto.response.PostResponse
import com.teamsparta.goover.domain.post.model.Post
import com.teamsparta.goover.domain.post.model.toResponse
import com.teamsparta.goover.domain.post.service.PostService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.format.DateTimeParseException


@RestController
@RequestMapping("/api/v1/posts")
class PostController(
    private val postService: PostService
) {

    @PostMapping(
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createPost(
        @ModelAttribute
        @Valid
        @RequestBody createRequest: PostCreateRequest
    ): ResponseEntity<PostResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.create(createRequest))
    }

    @DeleteMapping("{postId}")
    fun deletePost(
        @PathVariable postId: Long,
    ): ResponseEntity<String> {
        postService.delete(postId)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build()
    }

    @PutMapping("/{postId}",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updatePost(

        @PathVariable postId: Long,
        @ModelAttribute
        @RequestBody postUpdateRequest: PostUpdateRequest
    ): ResponseEntity<PostResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.update(postId, postUpdateRequest))
    }

    @GetMapping("/{postId}")
    fun getPost(@PathVariable postId: Long): ResponseEntity<PostResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.get(postId))
    }

    @GetMapping()
    fun getAllPosts(pageable: Pageable): ResponseEntity<Page<PostResponse>> {
        val posts = postService.getAllPost(pageable)
        return ResponseEntity.ok(posts.map { it.toResponse() })
    }

    @GetMapping("/title")
    fun getAllByTitle(@RequestParam title: String): ResponseEntity<List<PostResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.getAllByTitle(title))
    }
    @GetMapping("/created-at")
    fun getPostsByCreatedAt(@RequestParam("date") dateStr: String): ResponseEntity<List<Post>> {
        return try {
            val date = LocalDate.parse(dateStr)
            val startDate = date.atStartOfDay()
            val endDate = date.atTime(23, 59, 59)
            val posts = postService.getAllPostsByCreatedAt(startDate, endDate)
            ResponseEntity.ok(posts)
        } catch (e: DateTimeParseException) {
            ResponseEntity.badRequest().build()
        }
    }


    @PostMapping("/{postId}/like")
    fun likePost(
        @PathVariable postId: Long
    ) {
        val postResponse = postService.likePost(postId)
    }


}