package com.teamsparta.goover.domain.user.service

import jakarta.mail.MessagingException

interface EmailService {
    @Throws(MessagingException::class)
    fun sendHtmlMessage(to: String, subject: String, htmlContent: String)

    @Throws(MessagingException::class)
    fun sendVerificationEmail(to: String, verificationCode: String)
}
