package com.bopp.bopp.bopp

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import com.bopp.bopp.bopp.secret.getApiKey
import org.apache.el.parser.BooleanNode
import org.springframework.web.reactive.function.client.WebClient
import tools.jackson.databind.ObjectMapper
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.time.Duration

@RestController
class AiController {

    val systemPrompt = "you will respond to me either yes or no"
    val chatQuery = "do you like machine learning?"

    val body = """
{
  "model": "deepseek-chat",
  "messages": [
    { "role": "system", "content": "$systemPrompt" },
    { "role": "user", "content": "$chatQuery" }
  ],
  "thinking": { "type": "disabled" },
  "frequency_penalty": 0,
  "max_tokens": 4096,
  "presence_penalty": 0,
  "response_format": { "type": "text" },
  "stop": null,
  "stream": false,
  "stream_options": null,
  "temperature": 1,
  "top_p": 1,
  "tools": null,
  "tool_choice": "none",
  "logprobs": false,
  "top_logprobs": null
}
""".trimIndent()


    val mapper = ObjectMapper()
    @GetMapping("/v1/ai")
    fun getModelResponse() : ResponseEntity<String>{
        val apiKey = getApiKey()

        val requestBody = body

        val client = WebClient.create("https://api.deepseek.com")

        val responseFromLLM = client.post()
            .uri("/chat/completions")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer $apiKey")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(String::class.java)
            .block()  // blocking call to get the response

        // Return ResponseEntity with JSON
        return ResponseEntity.ok()
            .header("Content-Type", "application/json")
            .body(responseFromLLM)
    }
}




