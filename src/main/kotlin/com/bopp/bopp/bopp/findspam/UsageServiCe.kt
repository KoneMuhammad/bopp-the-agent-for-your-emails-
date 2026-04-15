package com.bopp.bopp.bopp.findspam

import com.bopp.bopp.bopp.UsageRepository
import com.bopp.bopp.bopp.findspam.Usage
import org.springframework.stereotype.Service

@Service
class UsageService(
    private val usageRepo: UsageRepository
) {

    fun canScan(userId: Long, isPaid: Boolean): Boolean {
        if (isPaid) return true

        val usage = usageRepo.findById(userId).orElse(Usage(userId))
        return usage.freeScansUsed < 2
    }

    fun incrementUsage(userId: Long) {
        val usage = usageRepo.findById(userId).orElse(Usage(userId))
        usage.freeScansUsed += 1
        usageRepo.save(usage)
    }
}