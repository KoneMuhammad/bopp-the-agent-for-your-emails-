package com.bopp.bopp.bopp.spamemail

import java.util.Base64

data class GmailMessage(
    val payload: Payload
)

data class Payload(
    val headers: List<Header>,
    val body: Body?,
    val parts: List<Payload>?
)

data class Header(
    val name: String,
    val value: String
)

data class Body(
    val data: String?
)



fun Payload.getReadableBody(): String {
    fun findData(p: Payload): String? {
        p.body?.data?.let { return it }
        p.parts?.forEach {
            val result = findData(it)
            if (result != null) return result
        }
        return null
    }

    val data = findData(this) ?: return ""
    return String(Base64.getUrlDecoder().decode(data))
}