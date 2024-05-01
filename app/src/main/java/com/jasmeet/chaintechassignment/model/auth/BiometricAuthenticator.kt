package com.jasmeet.chaintechassignment.model.auth

import android.content.Context
import android.preference.PreferenceManager
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.fragment.app.FragmentActivity

class BiometricAuthenticator(
    private val context: Context
) {

    private lateinit var promptInfo: PromptInfo
    private val biometricManager = BiometricManager.from(context)
    private lateinit var biometricPrompt :BiometricPrompt


    fun isBiometricAuthAvailable(): BiometricAuthStatus {
        return when(biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)){
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricAuthStatus.READY
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricAuthStatus.NOT_AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricAuthStatus.TEMP_NOT_AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricAuthStatus.AVAILABLE_BUT_NOT_ENROLLED
            else -> BiometricAuthStatus.NOT_AVAILABLE
        }
    }

    fun promptBioMetricAuth(
        title :String,
        subTitle :String ,
        negativeButtonText:String,
        fragmentActivity :FragmentActivity,
        onSuccess:(result:BiometricPrompt.AuthenticationResult)->Unit,
        onFailed: ()->Unit,
        onError: (errorCode :Int,errorString: String) ->Unit
    ){
        when(isBiometricAuthAvailable()){
            BiometricAuthStatus.NOT_AVAILABLE ->{
                onError(BiometricAuthStatus.NOT_AVAILABLE.id,"Not Available for this Device.")
                return

            }
            BiometricAuthStatus.TEMP_NOT_AVAILABLE ->{
                onError(BiometricAuthStatus.TEMP_NOT_AVAILABLE.id,"Not Available at this moment.")
                return

            }
            BiometricAuthStatus.AVAILABLE_BUT_NOT_ENROLLED ->{
                onError(BiometricAuthStatus.AVAILABLE_BUT_NOT_ENROLLED.id,"You should add a fingerprint or a face id first")
                return

            }
            else ->Unit
        }

        biometricPrompt = BiometricPrompt(
            fragmentActivity,
            object :BiometricPrompt.AuthenticationCallback(){
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess(result)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onFailed()

                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errorCode,errString.toString())
                }
            }
        )
        promptInfo = PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subTitle)
            .setNegativeButtonText(negativeButtonText)
            .build()

        biometricPrompt.authenticate(promptInfo)

    }
}

