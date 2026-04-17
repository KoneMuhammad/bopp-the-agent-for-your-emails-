package com.bopp.bopp.bopp.findspam


import com.bopp.bopp.bopp.OAuthRepository
import com.bopp.bopp.bopp.auth.OAuthToken
import com.bopp.bopp.bopp.secret.SecretService
import com.infisical.sdk.models.Secret
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import jakarta.servlet.http.HttpServletResponse
import org.apache.catalina.User
import org.gradle.internal.impldep.kotlinx.serialization.json.Json
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.reactive.function.client.WebClient
import tools.jackson.databind.ObjectMapper


@RestController
class AuthController(
    val authTokenService: EmailService,
    val secretService : SecretService,
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
       val token = authTokenService.getToken(code, secretService.clientId)
        val emails = authTokenService.getEmails(token.accessToken)
        return ResponseEntity.ok(emails)

    }

    @GetMapping("/spamEmail")
    fun getSpamEmails(){

    }


}


getting the most recent emails.

@Service
class EmailService(
) {
    val mapper = ObjectMapper()
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


    fun getEmails(accessToken: String): List<UserEmail> {

        val client = WebClient.create()

        val listResponse = client.get()
            .uri("https://gmail.googleapis.com/gmail/v1/users/me/messages?maxResults=10")
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        val root = mapper.readTree(listResponse)
        val messages = root["messages"] ?: return emptyList()

        val results = mutableListOf<UserEmail>()

        for (msg in messages) {

            val id = msg["id"].asString()

            val messageResponse = client.get()
                .uri("https://gmail.googleapis.com/gmail/v1/users/me/messages/{id}?format=full", id)
                .header("Authorization", "Bearer $accessToken")
                .retrieve()
                .bodyToMono(String::class.java)
                .block()

            val message = mapper.readValue(messageResponse, GmailMessage::class.java)

            val subject = message.payload.headers
                .find { it.name == "Subject" }
                ?.value ?: "(no subject)"

            val body = message.payload.getReadableBody()

            results.add(UserEmail(
                id = id,
                subject = subject,
                body = body
            ))
        }

        return results
    }
}


data class GmailMessage(
    val payload: Payload
)

data class Payload(
    val headers: List<Header>,
    val body: Body?,
    val parts: List<Payload>?
)

data class Header(
    val name: String,
    val value: String
)

data class Body(
    val data: String?
)


fun Payload.getReadableBody(): String {
    fun findData(p: Payload): String? {
        p.body?.data?.let { return it }
        p.parts?.forEach {
            val result = findData(it)
            if (result != null) return result
        }
        return null
    }

    val data = findData(this) ?: return ""
    return String(java.util.Base64.getUrlDecoder().decode(data))
}


data class UserEmail(
    val id: String,
    val subject: String,
    val body: String
)




