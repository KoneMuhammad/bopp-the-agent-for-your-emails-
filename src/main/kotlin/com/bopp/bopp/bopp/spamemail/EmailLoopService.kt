package com.bopp.bopp.bopp.spamemail

import com.bopp.bopp.bopp.auth.ClientTokenStore
import com.bopp.bopp.bopp.ai.AIService
import com.bopp.bopp.bopp.websocket.FrontendWebSocketHandler
import jakarta.annotation.PreDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

@Service
class EmailLoopService(
    private val clientTokenStore: ClientTokenStore,
    private val emailService: EmailService,
    private val aiService: AIService,
    private val frontendWebSocketHandler: FrontendWebSocketHandler,
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val jobsByClientId = ConcurrentHashMap<String, Job>()
    private val processedEmailIdsByClientId = ConcurrentHashMap<String, MutableSet<String>>()

    fun startLoop(clientId: String, pollIntervalMs: Long = 15_000L, maxRuns: Int = 5) {
        val accessToken = clientTokenStore.getAccessToken(clientId)
            ?: throw IllegalStateException("No Gmail access token found for clientId=$clientId")

        jobsByClientId.remove(clientId)?.cancel()

        processedEmailIdsByClientId.putIfAbsent(clientId, ConcurrentHashMap.newKeySet())

        jobsByClientId[clientId] = scope.launch {
            frontendWebSocketHandler.sendJson(
                clientId,
                mapOf(
                    "type" to "loop.started",
                    "clientId" to clientId,
                    "pollIntervalMs" to pollIntervalMs,
                    "maxRuns" to maxRuns,
                )
            )

            repeat(maxRuns) {
                try {
                    val fetchedEmails = emailService.getEmails(accessToken)
                    val processedIds = processedEmailIdsByClientId.getValue(clientId)
                    val newEmails = fetchedEmails.filterNot { processedIds.contains(it.id) }

                    frontendWebSocketHandler.sendJson(
                        clientId,
                        mapOf(
                            "type" to "emails.fetched",
                            "fetchedCount" to fetchedEmails.size,
                            "newCount" to newEmails.size,
                            "fetchedAt" to Instant.now().toString(),
                            "emails" to fetchedEmails,
                        )
                    )

                    if (newEmails.isNotEmpty()) {
                        val spamIds = aiService.getSpamEmailDecision(newEmails)

                        frontendWebSocketHandler.sendJson(
                            clientId,
                            mapOf(
                                "type" to "ai.classified",
                                "evaluatedCount" to newEmails.size,
                                "spamIds" to spamIds,
                                "evaluatedAt" to Instant.now().toString(),
                            )
                        )

                        if (spamIds.isNotEmpty()) {
                            emailService.markEmailsAsSpam(accessToken, spamIds)
                            frontendWebSocketHandler.sendJson(
                                clientId,
                                mapOf(
                                    "type" to "gmail.updated",
                                    "spamIds" to spamIds,
                                    "updatedAt" to Instant.now().toString(),
                                )
                            )
                        }

                        processedIds.addAll(newEmails.map { it.id })
                    }
                } catch (exception: Exception) {
                    frontendWebSocketHandler.sendJson(
                        clientId,
                        mapOf(
                            "type" to "loop.error",
                            "message" to (exception.message ?: "Unknown loop error"),
                        )
                    )
                }

                if (it < maxRuns - 1) {
                    delay(pollIntervalMs)
                }
            }

            frontendWebSocketHandler.sendJson(
                clientId,
                mapOf(
                    "type" to "loop.completed",
                    "clientId" to clientId,
                    "completedRuns" to maxRuns,
                )
            )
            jobsByClientId.remove(clientId)
        }
    }

    suspend fun stopLoop(clientId: String) {
        jobsByClientId.remove(clientId)?.cancelAndJoin()
        processedEmailIdsByClientId.remove(clientId)
        frontendWebSocketHandler.sendJson(clientId, mapOf("type" to "loop.stopped", "clientId" to clientId))
    }

    @PreDestroy
    fun shutdown() {
        jobsByClientId.values.forEach { it.cancel() }
    }
}
