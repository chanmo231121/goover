package com.teamsparta.goover.infra.security.jwt

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.util.WebUtils

@Component
class JwtCookieManager {

    fun addJwtCookie(response: HttpServletResponse, token: String) {
        val cookie = Cookie("JWT-TOKEN", token)
        cookie.maxAge = 7 * 24 * 60 * 60
        cookie.path = "/"
        response.addCookie(cookie)
    }

    fun getJwtTokenFromCookie(request: HttpServletRequest): String? {
        val cookie: Cookie? = WebUtils.getCookie(request, "JWT-TOKEN")
        return cookie?.value
    }
}