package com.bopp.bopp.bopp.controller

import com.bopp.bopp.bopp.UserRepository
import com.bopp.bopp.bopp.entity.Users
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    val repo: UserRepository
) {

    @PostMapping("/create-user")
    fun createUser(@RequestParam email: String): Users {
        return repo.save(Users(email = email))
    }
}
