package com.teamsparta.goover.global.exception.dto

data class BusinessLogicException(
    override val message: String? = "올바르지 않은 방식입니다."
) : RuntimeException()
