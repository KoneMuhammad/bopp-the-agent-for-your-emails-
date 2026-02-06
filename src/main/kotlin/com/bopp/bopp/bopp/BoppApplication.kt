package com.bopp.bopp.bopp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@SpringBootApplication
class BoppApplication

fun main(args: Array<String>) {
    runApplication<BoppApplication>(*args)
}

class WebSocketConfig: WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(WebSocketClientHandler(), "/ws")
    }
}

class WebSocketClientHandler: WebSocketHandler {
    override fun afterConnectionEstablished(session: WebSocketSession) {
        TODO("Not yet implemented")
    }

    override fun handleMessage(
        session: WebSocketSession,
        message: WebSocketMessage<*>
    ) {
        TODO("Not yet implemented")
    }

    override fun handleTransportError(
        session: WebSocketSession,
        exception: Throwable
    ) {
        TODO("Not yet implemented")
    }

    override fun afterConnectionClosed(
        session: WebSocketSession,
        closeStatus: CloseStatus
    ) {
        TODO("Not yet implemented")
    }

    override fun supportsPartialMessages(): Boolean {
        TODO("Not yet implemented")
    }
}



