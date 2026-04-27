package com.bopp.bopp.bopp.spamemail

import com.bopp.bopp.bopp.ai.AIService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class EmailController(
    private val aiService: AIService,
    private val emailService: EmailService,
    private val emailLoopService: EmailLoopService,
) {

    @PostMapping("/email/spam")
    fun setEmailsSpam(
        @RequestParam accessToken: String,
        @RequestBody emails: List<UserEmail>,
    ): ResponseEntity<List<String>> {
        val emailIds = aiService.getSpamEmailDecision(emails)
        if (emailIds.isNotEmpty()) {
            emailService.markEmailsAsSpam(accessToken, emailIds)
        }
        return ResponseEntity.ok(emailIds)
    }

    @PostMapping("/email/spam/loop/start")
    fun startSpamLoop(@RequestBody request: LoopControlRequest): ResponseEntity<Map<String, Any>> {
        emailLoopService.startLoop(
            clientId = request.clientId,
            pollIntervalMs = request.pollIntervalMs,
            maxRuns = request.maxRuns,
        )
        return ResponseEntity.ok(
            mapOf(
                "started" to true,
                "clientId" to request.clientId,
                "maxRuns" to request.maxRuns,
            )
        )
    }

    @DeleteMapping("/email/spam/loop")
    suspend fun stopSpamLoop(@RequestParam clientId: String): ResponseEntity<Map<String, Any>> {
        emailLoopService.stopLoop(clientId)
        return ResponseEntity.ok(mapOf("stopped" to true, "clientId" to clientId))
    }
}
