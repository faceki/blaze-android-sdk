package com.faceki.android

import android.app.Activity
import android.content.Intent
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.faceki.android.di.AppConfig
import com.faceki.android.di.AppModule
import com.faceki.android.util.FileManager


object FaceKi {
    @JvmStatic
    fun startKycVerification(
        context: Activity,
        clientId: String,
        clientSecret: String,
        workflowId: String,
        recordIdentifier: String?=null,
        kycResponseHandler: KycResponseHandler
    ) {
        context.runOnUiThread {
            if (clientId.isBlank()) {
                throw IllegalArgumentException("Invalid client Id")
            }
            if (clientSecret.isBlank()) {
                throw IllegalArgumentException("Invalid client secret")
            }
            if (workflowId.isBlank()) {
                throw IllegalArgumentException("Invalid workflow Id")
            }

            AppModule.initialize(context.application)
            FileManager.initialize(context.application)
            AppConfig.clientId = clientId
            AppConfig.clientSecret = clientSecret
            AppConfig.workflowId = workflowId
            AppConfig.recordIdentifier = recordIdentifier
            AppConfig.kycResponseHandler = kycResponseHandler

            context.startActivity(Intent(context, FaceKiActivity::class.java))
        }
    }

    @JvmStatic
    fun setCustomColors(colorMap: HashMap<ColorElement, ColorValue>) {
        AppConfig.setCustomColors(colorMap)
    }

    @JvmStatic
    fun setCustomIcons(iconMap: HashMap<IconElement, IconValue>) {
        AppConfig.setCustomIcons(iconMap)
    }


    enum class ColorElement {
        ButtonBackgroundColor, ButtonTextColor, PrimaryTextColor, SecondaryTextColor, TitleTextColor, BackgroundColor, ItemBackgroundColor
    }

    sealed class ColorValue {
        data class IntColor(@ColorInt val value: Int) : ColorValue()
        data class StringColor(val value: String) : ColorValue()
    }


    enum class IconElement {
        Logo, GuidanceImage
    }

    sealed class IconValue {
        data class Resource(@DrawableRes val resId: Int) : IconValue()
        data class Url(val url: String) : IconValue()
    }

}