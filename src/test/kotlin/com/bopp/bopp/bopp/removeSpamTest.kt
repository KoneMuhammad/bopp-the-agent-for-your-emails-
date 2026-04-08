package com.bopp.bopp.bopp

import com.bopp.bopp.bopp.user.putJacketOn
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class anotate {
    val on = "Success"

    @Test
    fun LaptopCreatedSuccess() {

        val result = putJacketOn("IPutItOn")

        assertEquals(on , result)
    }
}