package com.bopp.bopp.bopp.findspam

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "usage")
data class Usage(
    @Id
    val userId: Long,

    var freeScansUsed: Int = 0
)