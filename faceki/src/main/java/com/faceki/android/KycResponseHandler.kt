package com.faceki.android

interface KycResponseHandler {
    fun handleKycResponse(json: String?, result: VerificationResult)
}
