package com.bopp.bopp.bopp.spamemail

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class EmailController {


    @PostMapping("/email/spam")
    fun setEmailsSpam(@RequestBody emails: List<UserEmail>){


        //write to gmail set to spam using ai logic
        //deepseak service
    }

}