package com.bopp.bopp.bopp.spamemail

import com.bopp.bopp.bopp.ai.AIService
import kotlinx.coroutines.flow.flowOf
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient

@RestController
class EmailController(
    val aIService: AIService
) {


    @PostMapping("/email/spam")
    fun setEmailsSpam(@RequestBody emails: List<UserEmail>) {
        aIService.getSpamEmailDecision(emails)

    }

}

