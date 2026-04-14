package com.bopp.bopp.bopp.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "oauth_tokens")
data class OAuthToken(
    @Id
    val userId: Long,

    val accessToken: String,
    val refreshToken: String,
    val expiry: Long
)