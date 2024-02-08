package com.teamsparta.goover.infra.security


import com.teamsparta.goover.infra.security.jwt.JwtAuthenticationFilter
import com.teamsparta.goover.infra.security.jwt.JwtAuthenticationSuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val jwtAuthenticationSuccessHandler: JwtAuthenticationSuccessHandler
){

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .httpBasic { it.disable() } // BasicAuthenticationFilter, DefaultLoginPageGeneratingFilter, DefaultLogoutPageGeneratingFilter 제외
            .formLogin { it.disable() } // UsernamePassworedAuthenticationFilter, DefaultLoginPageGeneratingFilter, DefaultLogoutPageGeneratingFilter 제외
            .csrf { it.disable() } // CsrfFilter 제외
            .authorizeHttpRequests {
                it
                    .requestMatchers(
                    "/**"

                )
                    .permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .formLogin {
                it.successHandler(jwtAuthenticationSuccessHandler)
            }
            .build()
    }
}