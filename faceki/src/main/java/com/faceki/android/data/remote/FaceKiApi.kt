package com.faceki.android.data.remote

import com.faceki.android.data.remote.dto.response.GenerateTokenResponseDto
import com.faceki.android.data.remote.dto.response.WorkflowRulesResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


internal interface FaceKiApi {

    @GET(GENERATE_TOKEN)
    suspend fun generateToken(
        @Query("clientId") clientId: String,
        @Query("clientSecret") clientSecret: String,
    ): Response<GenerateTokenResponseDto>

    @GET(WORKFLOW_RULES)
    suspend fun getWorkflowRules(
        @Query("workflowId") workflowId: String,
    ): Response<WorkflowRulesResponseDto>


    @POST(KYC_VERIFICATION)
    @Multipart
    suspend fun verifyKyc(
        @Part("workflowId") workflowId: RequestBody,
        @Part("record_identifier") recordIdentifier: RequestBody?,
        @Part documents: List<MultipartBody.Part>,
    ): Response<ResponseBody>

    companion object {
        const val BASE_URL = "https://sdk.faceki.com/"
        const val GENERATE_TOKEN = "auth/api/access-token"
        const val WORKFLOW_RULES = "api/v3/workflows/rules"
        const val KYC_VERIFICATION = "api/v3/kyc_verification"
    }
}