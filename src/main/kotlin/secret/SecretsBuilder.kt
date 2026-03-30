package secret

import com.infisical.sdk.InfisicalSdk
import com.infisical.sdk.config.SdkConfig
import com.infisical.sdk.models.Secret

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