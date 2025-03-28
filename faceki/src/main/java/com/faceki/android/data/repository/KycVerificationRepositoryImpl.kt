package com.faceki.android.data.repository

import android.util.Log
import com.faceki.android.data.remote.FaceKiApi
import com.faceki.android.di.AppConfig
import com.faceki.android.domain.model.DocumentData
import com.faceki.android.domain.model.DocumentSide
import com.faceki.android.domain.model.VerificationResponse
import com.faceki.android.domain.repository.KycVerificationRepository
import com.faceki.android.util.Constants
import com.faceki.android.util.HttpStatusCodes
import com.faceki.android.util.KYCErrorCodes
import com.faceki.android.util.Resource
import com.faceki.android.util.asFile
import com.faceki.android.util.asMultipart
import com.faceki.android.util.createPartFromString
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException

internal class KycVerificationRepositoryImpl(
    private val faceKiApi: FaceKiApi
) : KycVerificationRepository {

    override suspend fun verifyKyc(
        verificationLink: String,
        workflowId: String,
        recordIdentifier: String?,
        selfieImage: File,
        documents: List<DocumentData>
    ): Resource<VerificationResponse> {


        return try {

            val multiPartDocs = mutableListOf<MultipartBody.Part>()

            val groupedDocuments = documents.groupBy { it.key }


            selfieImage.asMultipart("selfie").let { multiPartDocs.add(it) }

            var groupIndex = 1

            groupedDocuments.forEach { (key, group) ->
                // Sort the group to ensure front documents come before back documents
                val sortedGroup = group.sortedWith(compareBy({ it.documentSide }, { it.key }))

                sortedGroup.forEach { doc ->
                    val name = buildString {
                        append("document_")
                        append(groupIndex)
                        append("_")
                        append(if (doc.documentSide == DocumentSide.BACK) "back" else "front")
                    }

                    doc.imagePath?.asFile()?.asMultipart(partName = name)?.let {
                        multiPartDocs.add(it)
                    }
                }

                // Only increment the groupIndex after processing each unique key
                // This ensures that documents within the same group have the same index
                groupIndex++
            }

            val response = faceKiApi.verifyKyc(
                workflowId = createPartFromString(workflowId),
                verificationLink = createPartFromString(verificationLink),
                recordIdentifier = recordIdentifier?.let { createPartFromString(it) },
                documents = multiPartDocs
            )

            if (response.isSuccessful) {

                val jsonString = response.body()!!.string()

                val jsonObject: JsonObject = JsonParser.parseString(jsonString).asJsonObject

                val status = jsonObject.getAsJsonPrimitive("status").asBoolean
                val responseCode = jsonObject.getAsJsonPrimitive("code").asInt
                val isAccepted = jsonObject.getAsJsonObject("result")
                    ?.getAsJsonPrimitive("decision")?.asString == "ACCEPTED"


                when (responseCode) {
                    HttpStatusCodes.OK -> {

                        if (isAccepted && status) {
                            Resource.Success(
                                VerificationResponse(
                                    responseCode = KYCErrorCodes.SUCCESS,
                                    jsonBody = jsonObject.toString()
                                )
                            )
                        } else {
                            Resource.Success(
                                VerificationResponse(
                                    responseCode = Constants.INVALID_RESPONSE_CODE,
                                    errorMessage = "Couldn't verify Kyc"
                                )
                            )
                        }
                    }

                    KYCErrorCodes.PLEASE_TRY_AGAIN -> {
                        Resource.Success(
                            VerificationResponse(
                                responseCode = KYCErrorCodes.PLEASE_TRY_AGAIN,
                                jsonBody = jsonObject.toString(),
                                errorMessage = "Verification failed. Please try the verification process again."
                            )
                        )
                    }

                    KYCErrorCodes.FACE_CROPPED -> {
                        Resource.Success(
                            VerificationResponse(
                                responseCode = KYCErrorCodes.FACE_CROPPED,
                                jsonBody = jsonObject.toString(),
                                errorMessage = "The face in the image appears to be cropped or not fully visible."
                            )
                        )
                    }

                    KYCErrorCodes.FACE_TOO_CLOSED -> {
                        Resource.Success(
                            VerificationResponse(
                                responseCode = KYCErrorCodes.FACE_TOO_CLOSED,
                                jsonBody = jsonObject.toString(),
                                errorMessage = "The face in the image is too close to the camera, affecting the quality of the verification."
                            )
                        )
                    }

                    KYCErrorCodes.FACE_NOT_FOUND -> {
                        Resource.Success(
                            VerificationResponse(
                                responseCode = KYCErrorCodes.FACE_NOT_FOUND,
                                jsonBody = jsonObject.toString(),
                                errorMessage = "The system could not detect a face in the provided image."
                            )
                        )
                    }

                    KYCErrorCodes.FACE_CLOSED_TO_BORDER -> {
                        Resource.Success(
                            VerificationResponse(
                                responseCode = KYCErrorCodes.FACE_CLOSED_TO_BORDER,
                                jsonBody = jsonObject.toString(),
                                errorMessage = "The face in the image is too close to the border, affecting the quality of the verification."
                            )
                        )
                    }

                    KYCErrorCodes.FACE_TOO_SMALL -> {
                        Resource.Success(
                            VerificationResponse(
                                responseCode = KYCErrorCodes.FACE_TOO_SMALL,
                                jsonBody = jsonObject.toString(),
                                errorMessage = "The face in the image is too small for accurate verification."
                            )
                        )
                    }

                    KYCErrorCodes.POOR_LIGHT -> {
                        Resource.Success(
                            VerificationResponse(
                                responseCode = KYCErrorCodes.POOR_LIGHT,
                                jsonBody = jsonObject.toString(),
                                errorMessage = "The image quality is affected due to poor lighting conditions. Please ensure you are in a well-lit environment and try the verification process again."
                            )
                        )
                    }

                    else -> {
                        Resource.Success(
                            VerificationResponse(
                                responseCode = Constants.INVALID_RESPONSE_CODE,
                                jsonBody = jsonObject.toString(),
                                errorMessage = "Couldn't verify Kyc"
                            )
                        )
                    }
                }


            } else {
                Resource.Success(
                    VerificationResponse(
                        responseCode = Constants.INVALID_RESPONSE_CODE,
                        errorMessage = "Couldn't verify Kyc"
                    )
                )
            }


        } catch (e: NullPointerException) {
            Log.e(AppConfig.TAG, e.localizedMessage ?: "")
            Resource.Success(
                VerificationResponse(
                    responseCode = Constants.INVALID_RESPONSE_CODE,
                    errorMessage = "Couldn't verify Kyc"
                )
            )
        } catch (e: JsonSyntaxException) {
            Log.e(AppConfig.TAG, e.localizedMessage ?: "")
            Resource.Success(
                VerificationResponse(
                    responseCode = Constants.INVALID_RESPONSE_CODE,
                    errorMessage = "Couldn't verify Kyc"
                )
            )
        } catch (e: JsonParseException) {
            Log.e(AppConfig.TAG, e.localizedMessage ?: "")
            Resource.Success(
                VerificationResponse(
                    responseCode = Constants.INVALID_RESPONSE_CODE,
                    errorMessage = "Couldn't verify Kyc"
                )
            )
        } catch (e: IOException) {
            Log.e(AppConfig.TAG, e.localizedMessage ?: "")
            Resource.Success(
                VerificationResponse(
                    responseCode = Constants.INVALID_RESPONSE_CODE,
                    errorMessage = "Couldn't verify Kyc"
                )
            )
        } catch (e: HttpException) {
            Log.e(AppConfig.TAG, e.localizedMessage ?: "")
            Resource.Success(
                VerificationResponse(
                    responseCode = Constants.INVALID_RESPONSE_CODE,
                    errorMessage = "Couldn't verify Kyc"
                )
            )
        }
    }
}