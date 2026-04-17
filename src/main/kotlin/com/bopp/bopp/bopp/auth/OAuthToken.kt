package com.bopp.bopp.bopp.auth

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "oauth_tokens")
data class OAuthToken(
    @Id
    val userId: Long,

    val accessToken: String,
    val refresh_token: String?,
    val expires_in: Int,
    val scope: String,
    val token_type: String
)


