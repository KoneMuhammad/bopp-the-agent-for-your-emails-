package com.bopp.bopp.bopp.spamemail
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import tools.jackson.databind.ObjectMapper

@Service class EmailService {

    val mapper = ObjectMapper()

    fun getEmails(accessToken: String): List<UserEmail> {

        val client = WebClient.create()

        val listResponse = client.get()
            .uri("https://gmail.googleapis.com/gmail/v1/users/me/messages?maxResults=10")
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        val root = mapper.readTree(listResponse)
        val messages = root["messages"] ?: return emptyList()

        val results = mutableListOf<UserEmail>()

        for (msg in messages) {

            val id = msg["id"].asString()

            val messageResponse = client.get()
                .uri("https://gmail.googleapis.com/gmail/v1/users/me/messages/{id}?format=full", id)
                .header("Authorization", "Bearer $accessToken")
                .retrieve()
                .bodyToMono(String::class.java)
                .block()

            val message = mapper.readValue(messageResponse, GmailMessage::class.java)

            val subject = message.payload.headers
                .find { it.name == "Subject" }
                ?.value ?: "(no subject)"

            val body = message.payload.getReadableBody()

            results.add(UserEmail(
                id = id,
                subject = subject,
                body = body
            ))
        }

        return results
    }

    fun setEmailsToSpam(userEmails: List<UserEmail>) {

        /**
         * working on ui to decide what i want for the backend, looking up ai design tools to do so fe tools
         * webclient call to Gmail(token)->
         * webclient call to llm(email, prompt)->
         * logic(slop)->
         * webclient call to gmail(write)
         *
         * if newemailshowup step 2-3-4
         * [how] timed read to gmail or websocket gmail const give me emails
         */
    }

}
