package com.teamsparta.goover.infra.security.jwt

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationSuccessHandler(
    private val jwtPlugin: JwtPlugin,
    private val jwtCookieManager: JwtCookieManager
) :AuthenticationSuccessHandler{
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val token = jwtPlugin.generateAccessToken(
            subject = authentication.name,
            name = (authentication as UsernamePasswordAuthenticationToken).principal.toString(),
            role = "ROLE_USER"
        )
       jwtCookieManager.addJwtCookie(response, token)
    }
}