package com.faceki.android.data.remote.dto.response

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
internal data class WorkflowRulesResponseDto(
    @field:Json(name = "status") val status: Boolean? = false,
    @field:Json(name = "code") val code: Int? = null,
    @field:Json(name = "message") val message: String? = null,
    @field:Json(name = "result") val result: WorkflowRulesResponseResultDto? = null
)

@Keep
internal data class WorkflowRulesResponseResultDto(
    @field:Json(name = "_id") val id: String? = null,
    @field:Json(name = "branchId") val branchId: String? = null,
    @field:Json(name = "workflowId") val workflowId: String? = null,
    @field:Json(name = "companyId") val companyId: String? = null,
    @field:Json(name = "document_optional") val documentOptional: Boolean? = null,
    @field:Json(name = "documents") val documents: List<String> = emptyList()
)