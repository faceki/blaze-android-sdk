package com.faceki.android.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RuleResponseData(
    val id: String? = null,
    val branchId: String? = null,
    val workflowId: String? = null,
    val companyId: String? = null,
    val documentOptional: Boolean? = null,
    val documents: List<String> = emptyList()
) : Parcelable
