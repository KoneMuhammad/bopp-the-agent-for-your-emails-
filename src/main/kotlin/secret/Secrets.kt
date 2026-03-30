package secret

import com.infisical.sdk.InfisicalSdk
import com.infisical.sdk.config.SdkConfig

val infiscalSecretSdk = InfisicalSdk(SdkConfig.Builder()
    .withSiteUrl("")
    .build())

val secret = infiscalSecretSdk.Secrets().GetSecret(
    "ai_api_key",
    "822fe0ab-6677-4316-84ce-2bccce0cbb96",
    "Development",
    "/",
    false,
    false,
    "shared",
    )

