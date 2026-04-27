package com.bopp.bopp.bopp.auth

import com.bopp.bopp.bopp.security.SecretService
import com.bopp.bopp.bopp.spamemail.EmailService
import com.bopp.bopp.bopp.spamemail.UserEmail
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@RestController
class AuthController(
    private val authService: AuthService,
    private val clientTokenStore: ClientTokenStore,
    private val secretService: SecretService,
    private val emailService: EmailService,
) {

    @GetMapping("/auth/google")
    fun googleAuth(
        response: HttpServletResponse,
        @RequestParam frontendClientId: String,
    ) {
        val googleClientId = secretService.clientId.secretValue
        val redirectUri = URLEncoder.encode(
            "http://localhost:8080/auth/callback",
            StandardCharsets.UTF_8.toString()
        )

        val url = "https://accounts.google.com/o/oauth2/v2/auth" +
            "?client_id=$googleClientId" +
            "&redirect_uri=$redirectUri" +
            "&response_type=code" +
            "&scope=openid%20email%20profile%20https://www.googleapis.com/auth/gmail.modify" +
            "&access_type=offline" +
            "&state=${URLEncoder.encode(frontendClientId, StandardCharsets.UTF_8.toString())}"

        response.sendRedirect(url)
    }

    @GetMapping("/auth/callback")
    fun callback(
        @RequestParam code: String,
        @RequestParam(required = false) state: String?,
    ): ResponseEntity<List<UserEmail>> {
        val token = authService.getToken(code, secretService.clientId)
        val emails = emailService.getEmails(token.accessToken)

        state?.takeIf { it.isNotBlank() }?.let { clientId ->
            clientTokenStore.saveAccessToken(clientId, token.accessToken)
        }

        return ResponseEntity.ok(emails)
    }
}
