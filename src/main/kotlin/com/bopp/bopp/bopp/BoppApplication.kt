package com.bopp.bopp.bopp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BoppApplication

fun main(args: Array<String>) {
    runApplication<BoppApplication>(*args)
}
