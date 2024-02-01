package com.teamsparta.goover.domain.post.repository

import com.teamsparta.goover.domain.post.model.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository :JpaRepository<Post,Long>{

}