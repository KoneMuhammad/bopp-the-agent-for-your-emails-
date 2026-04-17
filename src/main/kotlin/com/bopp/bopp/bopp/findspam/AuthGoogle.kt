package com.bopp.bopp.bopp.findspam


import com.bopp.bopp.bopp.OAuthRepository
import com.bopp.bopp.bopp.auth.OAuthToken
import com.bopp.bopp.bopp.secret.SecretService
import com.infisical.sdk.models.Secret
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.reactive.function.client.WebClient


@RestController
class AuthController(
    val authTokenService: AuthTokenService,
    val secretService : SecretService,
    val tokenRepository : OAuthRepository
) {

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
    fun callback(@RequestParam code: String): String {
       val token = authTokenService.getToken(code, secretService.clientId)

        tokenRepository.save(token)
        return "Got code: $code"

    }


    @GetMapping("/spamEmail")
    fun getSpamEmails(){

    }


}

@Service
class AuthTokenService(
) {
    fun getToken(authCode: String, clientID: Secret): OAuthToken {

        val redirectUri = "https://bopp-backend.onrender.com"
        val body = "code=$authCode" +
                "&client_id=$clientID" +
                "&redirect_uri=${URLEncoder.encode(redirectUri, "UTF-8")}" +
                "&grant_type=authorization_code"

             val response = WebClient.builder()
            .baseUrl("https://oauth2.googleapis.com/token")
            .defaultHeader("Content-Type", "application/x-www-form-urlencoded")
            .build()
            .post()
            .bodyValue(body)
            .retrieve()
            .bodyToMono(OAuthToken::class.java)
            .block()


        return response!!
    }
}








