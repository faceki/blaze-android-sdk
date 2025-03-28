package com.faceki.android.presentation.verification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faceki.android.di.AppConfig
import com.faceki.android.domain.model.DocumentData
import com.faceki.android.domain.repository.KycVerificationRepository
import com.faceki.android.presentation.states.ScreenState
import com.faceki.android.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File


class KycVerificationViewModel(
    private val kycVerificationRepository: KycVerificationRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(ScreenState())
    val screenState get() = _screenState.asStateFlow()

    fun verifyKyc(
        selfieImage: File,
        documents: List<DocumentData>
    ) {

        viewModelScope.launch(Dispatchers.IO) {
            _screenState.value = ScreenState(
                isLoading = true
            )

            val resource = kycVerificationRepository.verifyKyc(
                verificationLink = AppConfig.verificationLink ?:"",
                workflowId = AppConfig.workflowId ?: "",
                recordIdentifier = AppConfig.recordIdentifier,
                selfieImage = selfieImage,
                documents = documents
            )


            when (resource) {
                is Resource.Loading -> {
                }

                is Resource.Error -> {
                    resource.message?.let { Log.e(AppConfig.TAG, it) }
                    _screenState.value = ScreenState(
                        isLoading = false,
                        isSuccess = false,
                        errorMessage = resource.message, responseCode = resource.data?.responseCode
                    )
                }

                is Resource.Success -> {
                    _screenState.value = ScreenState(
                        isLoading = false,
                        isSuccess = true,
                        errorMessage = resource.data?.errorMessage,
                        verificationResponse = resource.data?.jsonBody,
                        responseCode = resource.data?.responseCode
                    )
                }
            }
        }

    }

}