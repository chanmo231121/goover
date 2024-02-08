package com.teamsparta.goover.domain.user.model

import com.teamsparta.goover.domain.post.model.Post
import jakarta.persistence.*



@Entity
@Table(name = "app_user")
class User (

    @Column(name ="nickname", nullable = false)
    var name: String,

    @Column(name = "password", nullable = false)
    var password:String,

    @Column(name = "email", nullable = false)
    var email:String,

    @Column(name = "role", nullable = false)
    var role: Role = Role.USER,

    @OneToMany(mappedBy="user" , cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val posts: MutableList<Post> = mutableListOf()


)
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null




}

