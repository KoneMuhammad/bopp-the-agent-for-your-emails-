package com.bopp.bopp.bopp.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

@Component
class FrontendWebSocketHandler(
    private val objectMapper: ObjectMapper,
) : WebSocketHandler {

    private val sessionsByClientId = ConcurrentHashMap<String, MutableSet<WebSocketSession>>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val clientId = session.clientId() ?: run {
            session.close(CloseStatus.BAD_DATA)
            return
        }

        sessionsByClientId.computeIfAbsent(clientId) { ConcurrentHashMap.newKeySet() }.add(session)
        sendJson(clientId, mapOf("type" to "ws.connected", "clientId" to clientId))
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        if (message is TextMessage && message.payload == "ping") {
            session.sendMessage(TextMessage("""{"type":"pong"}"""))
        }
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        removeSession(session)
    }

    override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
        removeSession(session)
    }

    override fun supportsPartialMessages(): Boolean = false

    fun sendJson(clientId: String, payload: Any) {
        val message = TextMessage(objectMapper.writeValueAsString(payload))
        sessionsByClientId[clientId]
            ?.filter { it.isOpen }
            ?.forEach { it.sendMessage(message) }
    }

    private fun removeSession(session: WebSocketSession) {
        val clientId = session.clientId() ?: return
        sessionsByClientId[clientId]?.remove(session)
        if (sessionsByClientId[clientId].isNullOrEmpty()) {
            sessionsByClientId.remove(clientId)
        }
    }

    private fun WebSocketSession.clientId(): String? =
        uri?.query
            ?.split("&")
            ?.mapNotNull {
                val parts = it.split("=", limit = 2)
                if (parts.size == 2 && parts[0] == "clientId") parts[1] else null
            }
            ?.firstOrNull()
            ?.takeIf { it.isNotBlank() }
}
