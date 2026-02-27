package com.bopp.bopp.bopp

import com.sun.tools.javac.code.TypeAnnotationPosition.field
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.PingMessage
import org.springframework.web.socket.PongMessage
import org.springframework.web.socket.TextMessage
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

class WebSocketConfig(
    private val aiLayer: AiLayer
) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(WebSocketClientHandler(aiLayer), "/ws")
    }
}
class WebSocketClientHandler(
    private val aiLayer: AiLayer  // Inject it here
)  : WebSocketHandler {

    private val scope = CoroutineScope(Dispatchers.IO)  // Add this
    private val logger = LoggerFactory.getLogger(WebSocketClientHandler::class.java)

    override fun afterConnectionEstablished(session: WebSocketSession) {
        logger.info("WebSocket connection established: ${session.id}")

        // Optional: Send initial message to server
        session.sendMessage(TextMessage("Client connected"))

        // Optional: Store session if you need to reference it later
        // sessionMap[session.id] = session
    }

    override fun handleMessage(
        session: WebSocketSession,
        message: WebSocketMessage<*>
    ) {
        when (message) {
            is TextMessage -> {
                val payload = message.payload
                logger.info("Received text message: $payload")

                // Process the message
                scope.launch {
                    try {
                        // Example: Parse JSON
                        // val data = objectMapper.readValue(payload, MyDataClass::class.java)

                        // Handle the message
                        processMessage(session, payload)

                    } catch (e: Exception) {
                        logger.error("Error processing message: ${e.message}", e)
                        session.sendMessage(TextMessage("""{"error": "Failed to process message"}"""))
                    }
                }
            }

            is BinaryMessage -> {
                val payload = message.payload
                logger.info("Received binary message: ${payload.remaining()} bytes")

                // Handle binary data
                val bytes = ByteArray(payload.remaining())
                payload.get(bytes)
                // Process bytes...
            }

            is PingMessage -> {
                logger.debug("Received ping message")
                session.sendMessage(PongMessage())
            }

            is PongMessage -> {
                logger.debug("Received pong message")
                // Usually no action needed
            }
        }
    }

    override fun handleTransportError(
        session: WebSocketSession,
        exception: Throwable
    ) {
        logger.error("WebSocket transport error for session ${session.id}: ${exception.message}", exception)

        // Optional: Attempt reconnection logic
        // Optional: Notify application of error
        // Optional: Clean up resources

        try {
            session.close(CloseStatus.SERVER_ERROR)
        } catch (e: Exception) {
            logger.error("Error closing session after transport error", e)
        }
    }

    override fun afterConnectionClosed(
        session: WebSocketSession,
        closeStatus: CloseStatus
    ) {
        logger.info("WebSocket connection closed for session ${session.id}. Status: ${closeStatus.code} - ${closeStatus.reason}")

        // Clean up resources
        // sessionMap.remove(session.id)

        // Optional: Implement reconnection logic if needed
        when (closeStatus.code) {
            CloseStatus.NORMAL.code -> logger.info("Connection closed normally")
            CloseStatus.GOING_AWAY.code -> logger.info("Server going away")
            CloseStatus.SERVER_ERROR.code -> logger.warn("Server error occurred")
            else -> logger.warn("Connection closed with status: $closeStatus")
        }
    }

    override fun supportsPartialMessages(): Boolean {
        // Return false if you want to receive complete messages only
        // Return true if you want to handle fragmented messages
        return false
    }

    // Helper method to process messages
    private suspend fun processMessage(session: WebSocketSession, payload: String) {
        // Your business logic here
        logger.info("Processing message: $payload")
        // Alternate ways to do  logging pros and cons of each
        /**
         * the keys is that within the alternate ways of doing something or infinite ways of doing something
         * there are pros and cons of each
         */

        try {
            // Get AI response
            val aiResponse = aiLayer.chat(payload)

            // Send back to client
            session.sendMessage(TextMessage(aiResponse))

        } catch (e: Exception) {
            logger.error("Error getting AI response: ${e.message}", e)
            session.sendMessage(TextMessage("Error: Could not process your message"))
        }
    }
}


