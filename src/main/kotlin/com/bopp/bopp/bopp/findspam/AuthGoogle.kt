package com.bopp.bopp.bopp.findspam


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
import java.nio.charset.StandardCharsets

/**  ask springboot for response so i can send it after the geT
 *   in there i call google with a redirect resource which is my endpoint
 *   they handle 1 thing, they themselves go back to me to give me the auth
 *
 */
@RestController
class AuthController {

    @GetMapping("/auth/google")
    fun googleAuth(response: HttpServletResponse) {
        val clientId = "YOUR_CLIENT_ID"
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
        println("AUTH CODE: $code")
        return "Got code: $code"
    }


}

@Service
class getTokenService {
    fun getToken() {
        val clientIdOauth = gettsecret()

        val code = ""

        val body = "code=$code" +
                "&client_id=$clientIdOauth" +
                //redirect url one thats in the project
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








