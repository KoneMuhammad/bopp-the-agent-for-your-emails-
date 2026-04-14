package com.bopp.bopp.bopp.agent

data class LLMDecision(
    val id: String,
    val label: String,
    val confidence: Double
)