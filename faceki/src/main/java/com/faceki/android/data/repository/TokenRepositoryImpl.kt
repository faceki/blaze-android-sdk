package com.faceki.android.data.repository

import android.util.Log
import com.faceki.android.data.remote.FaceKiApi
import com.faceki.android.di.AppConfig
import com.faceki.android.domain.preferences.Preferences
import com.faceki.android.domain.repository.TokenRepository
import com.faceki.android.util.KYCErrorCodes
import com.faceki.android.util.Resource
import retrofit2.HttpException
import java.io.IOException

internal class TokenRepositoryImpl(
    private val faceKiApi: FaceKiApi, private val preferences: Preferences
) : TokenRepository {
    override suspend fun getBearerToken(clientId: String, clientSecret: String): Resource<String> {
        return try {

            val response = faceKiApi.generateToken(
                clientId = clientId, clientSecret = clientSecret
            )
            val body = response.body()!!

            if (response.isSuccessful) {

                when (body.responseCode) {
                    KYCErrorCodes.SUCCESS -> {
                        body.data?.let { data ->
                            val accessToken = data.accessToken ?: ""
                            preferences.saveToken(accessToken)
                            preferences.saveTokenTimestamp(System.currentTimeMillis())

                            Resource.Success(accessToken)
                        }
                        Resource.Success(body.data?.accessToken)
                    }

                    else -> {
                        Resource.Error("responseCode : ${body.responseCode} , statusCode : ${body.statusCode} , errorMessage = ${body.message} ,clientSecret = ${body.clientSecret} ,")
                    }
                }


            } else {
                Resource.Error("responseCode : ${body.responseCode} , statusCode : ${body.statusCode} , errorMessage = ${body.message} , clientSecret = ${body.clientSecret} ,")
            }

        } catch (e: NullPointerException) {
            Log.e(AppConfig.TAG, e.localizedMessage ?: "")
            Resource.Error(
                message = "Couldn't get Bearer Token"
            )
        } catch (e: IOException) {
            Log.e(AppConfig.TAG, e.localizedMessage ?: "")
            Resource.Error(
                message = "Couldn't get Bearer Token"
            )
        } catch (e: HttpException) {
            Log.e(AppConfig.TAG, e.localizedMessage ?: "")
            Resource.Error(
                message = "Couldn't get Bearer Token"
            )
        }
    }

    override fun getTokenTimestamp(): Long = preferences.getTokenTimestamp()
}