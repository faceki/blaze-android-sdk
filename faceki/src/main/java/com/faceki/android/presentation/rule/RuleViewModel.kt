package com.faceki.android.presentation.rule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faceki.android.di.AppConfig
import com.faceki.android.domain.repository.WorkflowRuleRepository
import com.faceki.android.presentation.states.ScreenState
import com.faceki.android.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RuleViewModel(
    private val ruleRepository: WorkflowRuleRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(ScreenState())
    val screenState get() = _screenState.asStateFlow()


    fun getRules(fetchFromRemote: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            _screenState.value = ScreenState(
                isLoading = true, isSuccess = false
            )

            when (val resource = ruleRepository.getWorkflowRules(
              verificationLink   = AppConfig.verificationLink!!,
                fetchFromRemote = fetchFromRemote
            )) {
                is Resource.Loading -> {}
                is Resource.Error -> {
                    resource.message?.let { Log.e(AppConfig.TAG, it) }
                    _screenState.value = ScreenState(
                        isLoading = false, isSuccess = false
                    )
                }

                is Resource.Success -> {
                    _screenState.value = ScreenState(
                        isLoading = false, isSuccess = true, ruleData = resource.data
                    )
                }
            }
        }
    }

}