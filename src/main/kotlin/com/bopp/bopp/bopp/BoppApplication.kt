package com.bopp.bopp.bopp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import javax.security.auth.Subject

@SpringBootApplication
class BoppApplication

fun main(args: Array<String>) {
    runApplication<BoppApplication>(*args)
}

