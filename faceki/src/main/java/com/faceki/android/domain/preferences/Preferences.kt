package com.faceki.android.domain.preferences

import com.faceki.android.domain.model.RuleResponseData

internal interface Preferences {
    fun saveToken(token: String)
    fun getToken(): String?
    fun saveTokenTimestamp(millsInSeconds: Long)
    fun getTokenTimestamp(): Long
    fun saveRuleResponse(ruleResponse: RuleResponseData)
    fun getRuleResponse(): RuleResponseData

    companion object {
        const val KEY_TOKEN = "token"
        const val KEY_TOKEN_TIMESTAMP = "token_timestamp"
        const val KEY_RULE_RESPONSE = "rule_response"
    }
}