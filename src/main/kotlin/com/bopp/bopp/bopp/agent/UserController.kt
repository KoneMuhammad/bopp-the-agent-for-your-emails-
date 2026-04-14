package com.bopp.bopp.bopp.agent

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    val repo: UserRepositoy
) {

    @PostMapping("/create-user")
    fun createUser(@RequestParam email: String): Users {
        return repo.save(Users(email = email))
    }
}
