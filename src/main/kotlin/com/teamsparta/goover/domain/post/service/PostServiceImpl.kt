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
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime


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
                .orElseThrow { IllegalArgumentException("권한이 없습니다.") }
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
        if (!userPrincipal.isAdmin() && userPrincipal.id != post.user?.id) {
            throw UnauthorizedException("게시글 삭제 권한이 없습니다.")
        }
        postRepository.delete(post)
        return "게시글이 성공적으로 삭제완료되었습니다."
    }


    @Transactional
    override fun update(postId: Long, updateRequest: PostUpdateRequest): PostResponse {
        val userPrincipal = SecurityContextHolder.getContext().authentication.principal as? UserPrincipal
            ?: throw UnauthorizedException("로그인이 필요합니다.")
        val post = postRepository.findByIdOrNull(postId) ?: throw ModelNotFoundException("post", postId)
        if (!userPrincipal.isAdmin() && userPrincipal.id != post.user?.id) {
            throw UnauthorizedException("게시글 수정 권한이 없습니다.")
        }
        val (title, description) = updateRequest
        post.title = title
        post.content = description
        return postRepository.save(post).toResponse()
    }

    override fun get(postId: Long): PostResponse {
        val post = postRepository.findByIdOrNull(postId) ?: throw ModelNotFoundException("post", postId)
        return post.toResponse()
    }

    override fun getAllPost(pageable: Pageable): Page<Post> {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable)
    }

    override fun getAllByTitle(title: String): List<PostResponse> {
        return postRepository.findByTitle(title).map { it.toResponse() }
    }

    override fun getAllPostsByCreatedAt(startDate: LocalDateTime, endDate: LocalDateTime): List<Post> {
        return postRepository.findAllByCreatedAtBetween(startDate, endDate)
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
