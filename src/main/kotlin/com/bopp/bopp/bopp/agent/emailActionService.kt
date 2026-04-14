package com.bopp.bopp.bopp.agent

@Service
class EmailActionService {

    fun applyActions(userId: Long, decisions: List<LLMDecision>) {

        decisions.forEach {
            when (it.label) {
                "spam" -> moveToSpam(it.id)
            }
        }
    }

    private fun moveToSpam(emailId: String) {
        // Call Gmail API here
    }
}