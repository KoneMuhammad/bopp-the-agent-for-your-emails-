package com.bopp.bopp.bopp

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse


//do a proper httpRequest
const val contentType = "Content-Type"
const val apiReturnFormat = "application/json"
const val systemPrompt = "you will respond to me either yes or no"
const val chatQuery = "is this email spam?"
lateinit var email: Email

val body = """
{
  "model": "deepseek-chat",
  "messages": [
    { "role": "system", "content": ${systemPrompt} },
    { "role": "user", "content": ${chatQuery} }
  ],
  "stream": false
}
""".trimIndent()


val httpRequest = HttpRequest.newBuilder()
    .uri(URI.create("https://api.deepseek.com/chat/completions"))
    .header("Content-Type", "application/json")
    .header("Authorization", "Bearer YOUR_API_KEY")
    .POST(BodyPublishers.ofString(body))
    .build()


val httpClient = HttpClient.newBuilder().build()

val httpResponse = HttpResponse.BodyHandlers.ofString()


















