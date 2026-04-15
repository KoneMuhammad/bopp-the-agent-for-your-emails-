package com.bopp.bopp.bopp.auth

import com.bopp.bopp.bopp.OAuthRepository
import com.bopp.bopp.bopp.auth.OAuthToken
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import java.net.URI
@RestController
class EmailOauthController(
    private val oauthRepo: OAuthRepository
) {

    @GetMapping("/auth/google")
    fun googleAuth(): ResponseEntity<Void> {
        val url = "https://accounts.google.com/o/oauth2/v2/auth?" +
                "client_id=YOUR_CLIENT_ID&" +
                "redirect_uri=http://localhost:8080/oauth/callback&" +
                "response_type=code&" +
                "scope=https://www.googleapis.com/auth/gmail.modify&" +
                "access_type=offline&" +
                "prompt=consent"

        return ResponseEntity.status(302).location(URI.create(url)).build()
    }

    @GetMapping("/oauth/callback")
    fun callback(
        @RequestParam code: String,
        @RequestParam state: String?
    ): String {

        val tokenResponse = WebClient.create()
            .post()
            .uri("https://oauth2.googleapis.com/token")
            .bodyValue(mapOf(
                "code" to code,
                "client_id" to "YOUR_CLIENT_ID",
                "client_secret" to "YOUR_CLIENT_SECRET",
                "redirect_uri" to "http://localhost:8080/oauth/callback",
                "grant_type" to "authorization_code"
            ))
            .retrieve()
            .bodyToMono(Map::class.java)
            .block()

        val accessToken = tokenResponse?.get("access_token") as String
        val refreshToken = tokenResponse["refresh_token"] as String
        val expiresIn = tokenResponse["expires_in"] as Int

        oauthRepo.save(
            OAuthToken(
                userId = 1,
                accessToken = accessToken,
                refreshToken = refreshToken,
                expiry = System.currentTimeMillis() + expiresIn * 1000
            )
        )

        return "Connected Gmail ✅"
    }
}