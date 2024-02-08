package com.teamsparta.goover.infra.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class UserPrincipal(
    val id: Long,
    val name: String,
    val authorities: Collection<GrantedAuthority>
) {
    constructor(id: Long, name: String, roles: Set<String>) : this(
        id,
        name,
        roles.map { SimpleGrantedAuthority("ROLE_$it") }
    )
    fun isAdmin(): Boolean {
        return authorities.any { it.authority == "ROLE_ADMIN" }
    }
}

