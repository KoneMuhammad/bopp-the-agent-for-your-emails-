package com.bopp.bopp.bopp.controller

import com.bopp.bopp.bopp.UserRepository
import com.bopp.bopp.bopp.service.EmailActionService
import com.bopp.bopp.bopp.service.LLMService
import com.bopp.bopp.bopp.service.UsageService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ScanController(
    private val gmailService: GmailService,
    private val llmService: LLMService,
    private val usageService: UsageService,
    private val actionService: EmailActionService,
    private val userRepo: UserRepository
) {

    @PostMapping("/scan")
    fun scan(@RequestParam userId: Long): Any {

        val user = userRepo.findById(userId).orElseThrow()

        if (!usageService.canScan(user.id, user.isPaid)) {
            return ResponseEntity.status(402).body("Payment Required")
        }

        val emails = gmailService.fetchEmails(user.id)
        val decisions = llmService.classifyBatch(emails)

        actionService.applyActions(user.id, decisions)

        usageService.incrementUsage(user.id)

        return decisions
    }
}