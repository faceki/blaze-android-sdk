package com.faceki.android.presentation.welcome

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faceki.android.di.AppConfig
import com.faceki.android.domain.repository.TokenRepository
import com.faceki.android.presentation.states.ScreenState
import com.faceki.android.util.Resource
import com.faceki.android.util.hasTokenExpired
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TokenViewModel(
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(ScreenState())
    val screenState get() = _screenState.asStateFlow()


    fun getBearerToken(clientId: String, clientSecret: String) {
        viewModelScope.launch(Dispatchers.IO) {

            _screenState.value = ScreenState(
                isLoading = true, token = null, isSuccess = false
            )



            when (val resource = tokenRepository.getBearerToken(
                clientId = clientId, clientSecret = clientSecret
            )) {
                is Resource.Loading -> {}
                is Resource.Error -> {
                    resource.message?.let { Log.e(AppConfig.TAG, it) }
                    _screenState.value = ScreenState(
                        isLoading = false, token = null, isSuccess = false
                    )
                }

                is Resource.Success -> {
                    Log.i(AppConfig.TAG, "token ${resource.data}")
                    _screenState.value = ScreenState(
                        isLoading = false, token = resource.data, isSuccess = true
                    )
                }

            }
        }

    }
}