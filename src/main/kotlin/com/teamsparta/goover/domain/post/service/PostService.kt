package com.teamsparta.goover.domain.post.service

import com.teamsparta.goover.api.post.dto.request.PostCreateRequest
import com.teamsparta.goover.api.post.dto.request.PostUpdateRequest
import com.teamsparta.goover.api.post.dto.response.PostResponse
import com.teamsparta.goover.domain.post.model.Post
import com.teamsparta.goover.infra.security.UserPrincipal
import java.time.LocalDateTime

interface PostService {
    fun create(request: PostCreateRequest):PostResponse
    fun delete(userPrincipal: UserPrincipal, postId: Long,):String
    fun update( postId: Long, updateRequest: PostUpdateRequest):PostResponse
    fun get(postId: Long,):PostResponse
    fun getAll(): List<Post>
    fun likePost(postId: Long)
    fun getAllByTitle(title:String):List<PostResponse>
    fun getPostsByCreatedAt(startDate: LocalDateTime, endDate: LocalDateTime): List<Post>



}