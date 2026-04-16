package com.bopp.bopp.bopp.findspam


import com.bopp.bopp.bopp.secret.SecretService
import com.infisical.sdk.models.Secret
import com.squareup.okhttp.Headers
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.reactive.function.client.WebClient
import java.net.http.HttpRequest


@RestController
class AuthController(
    val authTokenService: AuthTokenService,
    val secretService : SecretService
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
                "&scope=openid%20email%20profile" +
                "&access_type=offline"

        response.sendRedirect(url)
    }

    @GetMapping("/auth/callback")
    fun callback(@RequestParam code: String): String {
        authTokenService.getToken(code, secretService.clientId)
        println("AUTH CODE: $code")
        return "Got code: $code"
        /**
         * who do i return the code too?
         *
         * understand the google redirection
         *
         */

        /**
         *
         *
         */
    }


}

@Service
class AuthTokenService(
) {
    fun getToken(authCode: String, clientID: Secret) {

        val redirectUri = "https://bopp-backend.onrender.com"
        val body = "code=$authCode" +
                "&client_id=$clientID" +
                "&redirect_uri=${URLEncoder.encode(redirectUri, "UTF-8")}" +
                "&grant_type=authorization_code"

        WebClient.builder()
            .baseUrl("https://oauth2.googleapis.com/token")
            .defaultHeader("Content-Type", "application/x-www-form-urlencoded")
            .build()
            .post()
            .bodyValue(body)
            .retrieve()

    }

}








