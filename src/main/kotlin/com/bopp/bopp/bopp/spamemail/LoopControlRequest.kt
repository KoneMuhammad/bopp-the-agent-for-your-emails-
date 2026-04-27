package com.bopp.bopp.bopp.spamemail

data class LoopControlRequest(
    val clientId: String,
    val pollIntervalMs: Long = 15_000L,
    val maxRuns: Int = 5,
)
