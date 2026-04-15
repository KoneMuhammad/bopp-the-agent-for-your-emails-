package com.bopp.bopp.bopp.findspam

data class EmailDTO(
    val id: String,
    val subject: String,
    val from: String,
    val snippet: String
)