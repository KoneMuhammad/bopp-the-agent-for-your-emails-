package com.bopp.bopp.bopp

import io.gsonfire.util.Mapper
import jakarta.persistence.Convert
import jakarta.servlet.http.Cookie
import org.apache.coyote.Response
import org.springframework.boot.jackson.autoconfigure.JacksonProperties
import org.springframework.http.HttpRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.Mapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import tools.jackson.databind.ObjectMapper
import java.time.Duration
import kotlin.time.Duration.Companion.days

