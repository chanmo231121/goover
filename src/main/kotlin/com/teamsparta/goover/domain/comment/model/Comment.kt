package com.teamsparta.goover.domain.comment.model

import com.teamsparta.goover.api.comment.dto.Response.CommentResponse
import com.teamsparta.goover.domain.post.model.Post
import com.teamsparta.goover.domain.user.model.User
import com.teamsparta.goover.global.entity.BaseTimeEntity
import jakarta.persistence.*


@Entity
@Table
class Comment(

    @Column
    var description:String,

    @ManyToOne
    @JoinColumn(name = "post_id")
    val post: Post,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user:User

): BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null


}

fun Comment.toResponse(): CommentResponse {
    return CommentResponse(
        id=id!!,
        description = description,

    )
}