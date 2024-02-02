package com.teamsparta.goover.domain.post.service

import com.teamsparta.goover.api.post.dto.request.PostCreateRequest
import com.teamsparta.goover.api.post.dto.request.PostUpdateRequest
import com.teamsparta.goover.api.post.dto.response.PostResponse
import com.teamsparta.goover.domain.post.model.Post
import com.teamsparta.goover.domain.post.model.toResponse
import com.teamsparta.goover.domain.post.repository.PostRepository
import com.teamsparta.goover.domain.user.repository.UserRepository
import com.teamsparta.goover.global.exception.ModelNotFoundException
import com.teamsparta.goover.global.exception.UnauthorizedException
import com.teamsparta.goover.infra.security.UserPrincipal
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service


@Service
class PostServiceImpl(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : PostService {

    @Transactional
    override fun create(request: PostCreateRequest): PostResponse {

        val authentication = SecurityContextHolder.getContext().authentication
        val userPrincipal = authentication.principal as? UserPrincipal
            ?: throw UnauthorizedException("로그인이 필요합니다.")
        val post = Post(
            title = request.title,
            content = request.content,
            like = 0,
            user = userRepository.findById(userPrincipal.id)
                .orElseThrow { IllegalArgumentException("User with id ${userPrincipal.id} not found") }
        )
        val savedPost = postRepository.save(post)
        return savedPost.toResponse()
    }

    @Transactional
    override fun delete(userPrincipal: UserPrincipal, postId: Long): String {
        val postExists = postRepository.existsById(postId)
        if (!postExists) {
            return "게시글이 존재하지 않습니다."
        }
        val post = postRepository.findByIdOrNull(postId) ?: throw ModelNotFoundException("Post", postId)
        if (post.user == null || userPrincipal.id != post.user.id) {
            throw UnauthorizedException("다른 사용자의 게시글은 삭제할 수 없습니다.")
        }
        postRepository.delete(post)
        return "게시글이 성공적으로 삭제완료되었습니다."
    }


    @Transactional
    override fun update(postId: Long, request: PostUpdateRequest): PostResponse {
        val userPrincipal = SecurityContextHolder.getContext().authentication.principal as? UserPrincipal
            ?: throw UnauthorizedException("로그인이 필요합니다.")
        val post = postRepository.findByIdOrNull(postId) ?: throw ModelNotFoundException("post", postId)
        if (post.user == null || post.user.id != userPrincipal.id) {
            throw IllegalArgumentException("작성자만 게시글을 수정할 수 있습니다.")
        }
        val (title, description) = request
        post.title = title
        post.content = description
        return postRepository.save(post).toResponse()
    }

    override fun get(postId: Long): PostResponse {
        val post = postRepository.findByIdOrNull(postId) ?: throw ModelNotFoundException("post", postId)
        return post.toResponse()
    }

    override fun getAll(): List<PostResponse> {
        return postRepository.findAll().map { it.toResponse() }
    }


    @Transactional
    override fun likePost(postId: Long) {
        val userPrincipal = SecurityContextHolder.getContext().authentication.principal as? UserPrincipal
            ?: throw UnauthorizedException("로그인이 필요합니다.")
        val post = postRepository.findWithLikesById(postId)
            ?: throw ModelNotFoundException("Post", postId)
        val user = userRepository.findByIdOrNull(userPrincipal.id)
            ?: throw ModelNotFoundException("User", userPrincipal.id)

        val userLiked = post.likes?.contains(user) ?: false

        if (userLiked) {
            post.likes?.remove(user)
            post.like -= 1
        } else {
            post.likes?.add(user)
            post.like += 1
        }
        postRepository.save(post)
    }
}
