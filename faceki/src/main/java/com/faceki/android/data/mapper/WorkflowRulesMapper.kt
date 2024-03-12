package com.faceki.android.data.mapper

import com.faceki.android.data.remote.dto.response.WorkflowRulesResponseResultDto
import com.faceki.android.domain.model.RuleResponseData


internal fun WorkflowRulesResponseResultDto.toRuleResponseData(): RuleResponseData {
    return RuleResponseData(
        id = id,
        companyId = companyId,
        documentOptional = documentOptional,
        documents = documents
    )
}

