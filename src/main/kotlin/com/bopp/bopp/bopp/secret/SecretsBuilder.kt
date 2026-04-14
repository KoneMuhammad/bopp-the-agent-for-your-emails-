package com.bopp.bopp.bopp.secret

import com.infisical.sdk.InfisicalSdk
import com.infisical.sdk.config.SdkConfig
import com.infisical.sdk.models.Secret
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import tools.jackson.databind.ObjectMapper
import java.awt.Color
import java.net.http.HttpClient
import java.net.http.HttpResponse
import java.time.Duration

val infiscalSecretSdk = InfisicalSdk(SdkConfig.Builder()
    .build())


fun getApiKey(): Secret{
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


@Component
class SpringBootManagaement {


    fun calculateFunds(){}
}


class baseexAmple(managementexample: SpringBootManagaement){
}