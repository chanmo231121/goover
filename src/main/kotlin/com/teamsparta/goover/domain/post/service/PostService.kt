package com.teamsparta.goover.domain.post.service

import com.teamsparta.goover.api.post.dto.request.PostCreateRequest
import com.teamsparta.goover.api.post.dto.request.PostUpdateRequest
import com.teamsparta.goover.api.post.dto.response.PostResponse
import com.teamsparta.goover.infra.security.UserPrincipal

interface PostService {
    fun create(request: PostCreateRequest):PostResponse
    fun delete(userPrincipal: UserPrincipal, postId: Long,):String
    fun update( postId: Long, updateRequest: PostUpdateRequest):PostResponse
    fun get(postId: Long,):PostResponse
    fun getAll():List<PostResponse>
    fun likePost(postId: Long)

}