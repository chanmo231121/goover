package com.teamsparta.goover.domain.user.service
// EmailServiceImpl.kt

import jakarta.mail.MessagingException
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl(
    private val javaMailSender: JavaMailSender,
    @Value("\${auth.jwt.setFrom}") private val setFrom: String,
) : EmailService {

    @Throws(MessagingException::class)
    override fun sendHtmlMessage(to: String, subject: String, htmlContent: String) {
        val message = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")

        helper.setFrom(setFrom)
        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(htmlContent, true)  // Set the second parameter to true to use HTML content.

        javaMailSender.send(message)
    }

    @Throws(MessagingException::class)
    override fun sendVerificationEmail(to: String, verificationCode: String) {
        val subject = "회원가입 인증 메일"
        val htmlContent = """
            <div style='margin:100px;'>
                <h1>안녕하세요. 회원가입을 환영합니다!</h1>
                <p>아래의 코드를 사용하여 회원가입을 완료하세요.</p>
                <p>인증 코드: <strong>$verificationCode</strong></p>
                  <p>아래 링크를 클릭하여 가입을 완료하세요.</p>
                <a href='http://your-domain.com/complete-sign-up?email=$to&code=$verificationCode'>
                    가입 완료 링크
            </div>
        """.trimIndent()

        sendHtmlMessage(to, subject, htmlContent)
    }
}
