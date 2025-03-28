package com.faceki.android.data.repository

import android.util.Log
import com.faceki.android.data.mapper.toRuleResponseData
import com.faceki.android.data.remote.FaceKiApi
import com.faceki.android.di.AppConfig
import com.faceki.android.domain.model.RuleResponseData
import com.faceki.android.domain.preferences.Preferences
import com.faceki.android.domain.repository.WorkflowRuleRepository
import com.faceki.android.util.HttpStatusCodes
import com.faceki.android.util.Resource
import com.faceki.android.util.isTrue
import retrofit2.HttpException
import java.io.IOException

internal class WorkflowRuleRepositoryImpl(
    private val faceKiApi: FaceKiApi, private val preferences: Preferences
) : WorkflowRuleRepository {
    override suspend fun getWorkflowRules(
        verificationLink: String,
        fetchFromRemote: Boolean
    ): Resource<RuleResponseData> {
        return try {

            if (!fetchFromRemote) {
                return Resource.Success(preferences.getRuleResponse())
            }
            val response = faceKiApi.getWorkflowRules(
                verificationLink = verificationLink
            )
            val body = response.body()!!

            if (response.isSuccessful && body.status.isTrue()) {

                when (body.code) {
                    HttpStatusCodes.OK -> {
                        val ruleResponse = body.result!!.toRuleResponseData()
                        AppConfig.workflowId = ruleResponse.workflowId
                        preferences.saveRuleResponse(ruleResponse)
                        Resource.Success(ruleResponse)
                    }

                    else -> {
                        Resource.Error("Couldn't get Kyc Rules")
                    }
                }


            } else {
                Resource.Error("Couldn't get Kyc Rules")
            }

        } catch (e: NullPointerException) {
            Log.e(AppConfig.TAG, e.localizedMessage ?: "")
            Resource.Error(
                message = "Couldn't get Kyc Rules"
            )
        } catch (e: IOException) {
            Log.e(AppConfig.TAG, e.localizedMessage ?: "")
            Resource.Error(
                message = "Couldn't get Kyc Rules"
            )
        } catch (e: HttpException) {
            Log.e(AppConfig.TAG, e.localizedMessage ?: "")
            Resource.Error(
                message = "Couldn't get Kyc Rules"
            )
        }
    }
}