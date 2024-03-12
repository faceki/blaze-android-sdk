package com.faceki.android

import androidx.activity.OnBackPressedCallback
import com.faceki.android.databinding.ActivityFaceKiBinding
import com.faceki.android.di.AppConfig
import com.faceki.android.presentation.base.BaseActivity
import com.faceki.android.util.isTrue

internal val GETTING_STARTED_FRAGMENT = R.id.gettingStartedFragment

internal class FaceKiActivity :
    BaseActivity<ActivityFaceKiBinding>(ActivityFaceKiBinding::inflate) {

    override fun getNavControllerViewId(): Int = R.id.faceki_nav_host_fragment

    override fun setupViews() {

    }


    override fun getOnBackPressedCallback(): OnBackPressedCallback {
        return object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AppConfig.removeLastDocument()
                when (navController?.currentDestination?.id) {
                    GETTING_STARTED_FRAGMENT -> {
                        finishAndRemoveTask()
                        AppConfig.kycResponseHandler?.handleKycResponse(
                            null,
                            VerificationResult.ResultCanceled
                        )
                    }

                    else -> {
                        finish()
                        AppConfig.kycResponseHandler?.handleKycResponse(
                            null,
                            VerificationResult.ResultCanceled
                        )
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController?.navigateUp().isTrue() || super.onSupportNavigateUp()
    }
}