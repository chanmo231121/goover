package com.teamsparta.goover.domain.post.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.teamsparta.goover.api.post.dto.response.PostResponse
import com.teamsparta.goover.domain.comment.model.Comment
import com.teamsparta.goover.domain.user.model.User
import com.teamsparta.goover.global.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "post")
class Post(

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "content", nullable = false)
    var content: String,

    @Column(name = "like_count")
    var like: Long,

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "post_likes",
        joinColumns = [JoinColumn(name = "post_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    var likes: MutableList<User> = mutableListOf(),

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_userid", nullable = false)
    val user: User,

    @JsonIgnore
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    val comments: MutableList<Comment> = mutableListOf()

) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null


}

fun Post.toResponse(): PostResponse  {
    return PostResponse(
        id = id!!,
        title = title,
        content = content
    )

}

