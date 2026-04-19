package com.bopp.bopp.bopp.security

import com.infisical.sdk.InfisicalSdk
import com.infisical.sdk.config.SdkConfig
import com.infisical.sdk.models.Secret
import org.springframework.stereotype.Service


@Service
class SecretService {


    val clientId: Secret by lazy {
        getClientID()
    }
    val infiscalSecretSdk = InfisicalSdk(
        SdkConfig.Builder()
            .build()
    )

    fun getApiKey(): Secret {
        val secret = infiscalSecretSdk.Secrets().GetSecret(
            "ai_api_key",
            "822fe0ab-6677-4316-84ce-2bccce0cbb96",
            "Development",
            "/",
            false,
            false,
            "shared",
        )
        return secret
    }

    fun getClientID():
            Secret {
        val secret = infiscalSecretSdk.Secrets().GetSecret(
            "GOOGLE_CLIENT_ID",
            "822fe0ab-6677-4316-84ce-2bccce0cbb96",
            "Development",
            "/",
            false,
            false,
            "shared",
        )
        return secret
    }

    fun getClientSecret():
            Secret {
        val secret = infiscalSecretSdk.Secrets().GetSecret(
            "GOOGLE_CLIENT_SECRET",
            "822fe0ab-6677-4316-84ce-2bccce0cbb96",
            "Development",
            "/",
            false,
            false,
            "shared",
        )
        return secret
    }

}