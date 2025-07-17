package com.faceki

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.faceki.android.FaceKi
import com.faceki.android.KycResponseHandler
import com.faceki.android.VerificationResult

class MainActivity : AppCompatActivity() {

    private val kycResponseHandler: KycResponseHandler = object : KycResponseHandler {
        override fun handleKycResponse(
            json: String?,
            result: VerificationResult
        ) {
            when (result) {
                is VerificationResult.ResultOk -> {
                    findViewById<TextView>(R.id.tv_result).text = result.toString()
                    Log.e("TAG", "handleKycResponse: ===========================$result", )
                    Toast.makeText(this@MainActivity, "ResultOk", Toast.LENGTH_SHORT).show()
                }

                is VerificationResult.ResultCanceled -> {
                    Toast.makeText(this@MainActivity, "ResultCanceled", Toast.LENGTH_SHORT).show()
                }
            }
            Log.e("TAG", "handleKycResponse: ===========================$json", )

            Toast.makeText(this@MainActivity, "kycResponseHandler $json", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.open_library).setOnClickListener {
            findViewById<TextView>(R.id.tv_result).text = null
            FaceKi.setCustomIcons(
                iconMap = hashMapOf(
                    FaceKi.IconElement.Logo to FaceKi.IconValue.Resource(R.drawable.ic_launcher_background)
                )
            )
            FaceKi.setCustomColors(
                colorMap = hashMapOf(
                    FaceKi.ColorElement.BackgroundColor to FaceKi.ColorValue.StringColor("#FFFFFF")
                )
            )
            FaceKi.startKycVerification(
                context = this@MainActivity,
                verificationLink = TEST_VERIFICATION_LINK,
                recordIdentifier = TEST_RECORD_IDENTIFIER,
                kycResponseHandler = kycResponseHandler
            )
        }
    }

    companion object {
        private const val TEST_RECORD_IDENTIFIER = "123"
        private const val TEST_VERIFICATION_LINK = "b1031cff-4ecd-46dd-9aae-a2be2336a123"
    }

}