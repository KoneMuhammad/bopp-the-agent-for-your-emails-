package com.bopp.bopp.bopp

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest


val request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.github.com"))//add deepseak
    .GET()
    .build()

class AiModel(val response: String, val SystemPrompt: String,)


fun llmcall(){ httpClient.send<>()}


















