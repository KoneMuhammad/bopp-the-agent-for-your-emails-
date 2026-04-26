package com.bopp.bopp.bopp.spamemail

import kotlinx.coroutines.flow.flowOf
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient

@RestController
class EmailController {


    @PostMapping("/email/spam")
    fun setEmailsSpam(@RequestBody emails: List<UserEmail>) {
        llm.agenticCall(emails, "")//system prompt with api
        /**
         * llm.call returns gmailmodify + decisions to emails
         * llm gets Called on no click ->
         * llm does alot of work and outputs each of its lottawork
         * with toolcalls to change its previous work
         * it has the context in its chat
         * its like a flow or stream
         * my prog accepts the results, removes the fluff
         * into the actual toolcall
         *
         * llm call -> flow of output
         * i transfer output to my webclient.call(xyz)
         *
         *
         *
         */
    }

}

fun llmcall(result): ResponseEntity {
    constant = thellm()
    result =  WebClient.builder().build().post().body(constant)

    return ResponseEntity.ok(result)
    llmcall()
}

this is my thoughts on how agentic things aRe done, i call the llm
        i turn in what it returns to the actual post body
        then i return that body ie as a json or somehting
        then i call the function again,
        beforee adding extra stuff like gaurD rails or something
        is this the correct appraoch to llms that constantly run
        or work on their own?

