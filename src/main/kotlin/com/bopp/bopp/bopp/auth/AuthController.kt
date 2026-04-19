package com.bopp.bopp.bopp.auth


import com.bopp.bopp.bopp.OAuthRepository
import com.bopp.bopp.bopp.security.SecretService
import com.bopp.bopp.bopp.spamemail.EmailService
import com.bopp.bopp.bopp.spamemail.UserEmail
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import org.springframework.web.bind.annotation.RequestParam
import tools.jackson.databind.ObjectMapper


@RestController
class AuthController(
    val authService: AuthService,
    val secretService : SecretService,
    val emailService: EmailService,
    val tokenRepository : OAuthRepository
) {

    val mapper = ObjectMapper()

    @GetMapping("/auth/google")
    fun googleAuth(response: HttpServletResponse) {
        val clientId = secretService.clientId
        val redirectUri = URLEncoder.encode(
            //switch to render domain
            "http://localhost:8080/auth/callback",
            StandardCharsets.UTF_8.toString()
        )

        val url = "https://accounts.google.com/o/oauth2/v2/auth" +
                "?client_id=$clientId" +
                "&redirect_uri=$redirectUri" +
                "&response_type=code" +
                "&scope=openid%20email%20profile%20https://www.googleapis.com/auth/gmail.modify" +
                "&access_type=offline"

        response.sendRedirect(url)
    }

    @GetMapping("/auth/callback")
    fun callback(@RequestParam code: String): ResponseEntity<List<UserEmail>> {
       val token = authService.getToken(code, secretService.clientId)

        val emails = emailService.getEmails(token.accessToken)
        return ResponseEntity.ok(emails)

    }

}
