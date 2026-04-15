package com.bopp.bopp.bopp.findspam

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "email_actions")
data class EmailAction(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val userId: Long,
    val emailId: String,
    val action: String,
    val confidence: Double,
    val timestamp: Long = System.currentTimeMillis()
)