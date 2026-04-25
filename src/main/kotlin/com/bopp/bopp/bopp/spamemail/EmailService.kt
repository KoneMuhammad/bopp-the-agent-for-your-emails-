package com.bopp.bopp.bopp.spamemail
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import tools.jackson.databind.ObjectMapper

@Service class EmailService {
//maybe seperate the two to be able to do lazy

    //one to get the emails. the other to convert from email to actual useemail object


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

    }

}
