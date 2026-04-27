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
        val webClient = WebClient.builder().build()

        val emailIds = aIService.getSpamEmailDecision(emails)

        emailIds.forEach { id ->
            webClient.post()
                .uri("https://gmail.googleapis.com/gmail/v1/users/me/messages/{id}/modify", id)
                .header("Authorization", "Bearer TOKEN")
                .bodyValue(
                    mapOf(
                        "addLabelIds" to listOf("SPAM"),
                        "removeLabelIds" to listOf("INBOX")
                    )
                )
                .retrieve()
                .bodyToMono(String::class.java)
                .block()
        }
}

    }




client is sending json emails to me
im sending that to llm
llm decididing weather or not the emails are  spam
llm produce list of ids
i bring list to gmail api
gmail changes emails to spam
,, now make it agentiC,
key is in the domain


what would make it agentic is if i had the llm
constantly producing outputs,
i send 1 prompt llm produces 1 or more outputs that i consume

llm produce alot without my pRompt and i just accept it

2 keys

the accepting it part, and
constant input - constant output

now how to constantly input and accept the constant output
repeat function
or a scheduled to constantly ask the llm
Also Scheduled to constantly accept output
with that scheduled id have to saVe the output to, a db each users local db, and fEtch from there


how to repeat and also return? without backend



how ThatNO WAY INTERG
        one way to do that is to