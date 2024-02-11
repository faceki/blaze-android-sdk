package com.faceki.android.domain.repository

import com.faceki.android.domain.model.DocumentData
import com.faceki.android.domain.model.VerificationResponse
import com.faceki.android.util.Resource
import java.io.File

interface KycVerificationRepository {

    suspend fun verifyKyc(
        workflowId: String,
        recordIdentifier: String?,
        selfieImage: File,
        documents: List<DocumentData>
    ): Resource<VerificationResponse>
}