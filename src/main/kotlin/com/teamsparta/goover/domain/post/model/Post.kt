package com.teamsparta.goover.domain.post.model

import com.teamsparta.goover.api.post.dto.response.PostResponse
import com.teamsparta.goover.domain.user.model.User
import com.teamsparta.goover.global.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "post")
class Post (

    @Column(name = "title", nullable = false)
    var title:String,

    @Column(name = "content", nullable = false)
    var content:String,


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_userid", nullable = false)
    val user: User,


    ): BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

}

fun Post.toResponse():PostResponse{
    return PostResponse(
        id=id!!,
        title = title,
        content = content
    )
}