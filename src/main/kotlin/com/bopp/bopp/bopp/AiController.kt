package com.bopp.bopp.bopp

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import secret.getApiKey
import tools.jackson.databind.ObjectMapper
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse

@RestController
class AiController {


    val mapper = ObjectMapper()
    @GetMapping("/v1/ai")
    fun getModelResponse() : ResponseEntity<HttpResponse<String>>{
        val httpRequest = HttpRequest.newBuilder()
            .uri(URI.create("https://api.deepseek.com/chat/completions"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${getApiKey()}")
            .POST(BodyPublishers.ofString(body))
            .build()


        return ResponseEntity.ok().body(sendModelInstructions(httpRequest))
    }
}