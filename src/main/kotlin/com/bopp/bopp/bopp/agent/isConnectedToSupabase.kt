package com.bopp.bopp.bopp.agent

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient

@RestController
class UserController(
    val repo: UserRepositoy
) {

    @PostMapping("/create-user")
    fun createUser(@RequestParam email: String): Users {
        return repo.save(Users(email = email))
    }
}
//user repo should save useer into supabase and return
//what was saved
/**
 * Supabase
 *
 * function not connected to supabase
 *
 * make assertions make correct condifent assertions
 */
