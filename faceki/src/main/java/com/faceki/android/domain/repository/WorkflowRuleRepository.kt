package com.faceki.android.domain.repository

import com.faceki.android.domain.model.RuleResponseData
import com.faceki.android.util.Resource


interface WorkflowRuleRepository {
    suspend fun getWorkflowRules(
        verificationLink: String,
        fetchFromRemote: Boolean
    ): Resource<RuleResponseData>
}