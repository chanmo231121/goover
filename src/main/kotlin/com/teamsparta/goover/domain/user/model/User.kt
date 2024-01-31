package com.teamsparta.goover.domain.user.model

import jakarta.persistence.*



@Entity
@Table(name = "app_user")
class User (

    @Column(name ="nickname", nullable = false)
    var name: String,

    @Column(name = "password", nullable = false)
    var password:String,

    @Column(name = "confirm_password", nullable = false)
    var confirmpassword:String,

    @Column(name = "email", nullable = false)
    var email:String,

    @Column(name = "role", nullable = false)
    var role: Role = Role.USER,


)
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null



}

