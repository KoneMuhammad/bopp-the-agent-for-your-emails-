package com.bopp.bopp.bopp.auth

import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class ClientTokenStore {

    private val accessTokensByClientId = ConcurrentHashMap<String, String>()

    fun saveAccessToken(clientId: String, accessToken: String) {
        accessTokensByClientId[clientId] = accessToken
    }

    fun getAccessToken(clientId: String): String? = accessTokensByClientId[clientId]

    fun clear(clientId: String) {
        accessTokensByClientId.remove(clientId)
    }
}
