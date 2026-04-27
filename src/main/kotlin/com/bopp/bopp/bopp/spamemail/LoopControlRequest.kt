package com.bopp.bopp.bopp.spamemail

data class LoopControlRequest(
    val clientId: String,
    val accessToken: String,
    val pollIntervalMs: Long = 15_000L,
)
