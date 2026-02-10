package com.bopp.bopp.bopp

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "ai")
data class AIConfig(
    var apiKey: String = "",
    var baseUrl: String = "https://api.deepseek.com/v1",
    var model: String = "deepseek-chat"
)
