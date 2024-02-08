package com.teamsparta.goover.domain.user.model

import com.teamsparta.goover.domain.post.model.Post
import com.teamsparta.goover.global.exception.StringMutableListConverter
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

    @Column(name = "profile_pic_url", nullable = false)
    @Convert(converter = StringMutableListConverter::class)
    var profilePicUrl: MutableList<String> = mutableListOf("https://imgur.com/S8jQ6wN"),

    @OneToMany(mappedBy="user" , cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val posts: MutableList<Post> = mutableListOf()


)
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null




}

