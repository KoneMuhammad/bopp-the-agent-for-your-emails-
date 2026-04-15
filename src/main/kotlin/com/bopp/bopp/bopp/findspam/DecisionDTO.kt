package com.bopp.bopp.bopp.findspam

data class LLMDecision(
    val id: String,
    val label: String,
    val confidence: Double
)