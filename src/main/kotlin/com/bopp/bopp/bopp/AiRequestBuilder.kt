package com.bopp.bopp.bopp

import secret.getApiKey
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse

val httpClient = HttpClient.newBuilder().build()

const val systemPrompt = "you will respond to me either yes or no"
const val chatQuery = "is this email spam?"
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
    val httpResponse = HttpResponse.BodyHandlers.ofString()


fun sendModelInstructions(httpRequest: HttpRequest ): HttpResponse<String>{
    val responseBody = httpClient.send(httpRequest, httpResponse)
   return responseBody
}














