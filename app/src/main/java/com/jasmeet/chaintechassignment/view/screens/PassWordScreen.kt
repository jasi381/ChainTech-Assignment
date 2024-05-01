package com.jasmeet.chaintechassignment.view.screens

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import javax.crypto.KeyGenerator


@Composable
fun PassWordScreen3() {
    val passwordState = remember { mutableStateOf(TextFieldValue()) }
    var isLocked by remember { mutableStateOf(true) }
    val context = LocalContext.current

    val masterKeyAlias = "master_key"
    val isVerified = remember {
        mutableStateOf(false)
    }

    if (isLocked) {
        SetPasswordUI(passwordState, context, masterKeyAlias) {
            isLocked = false
        }
    } else {
        EnterPasswordUI( context, masterKeyAlias) {
            isVerified.value = it
        }
    }

    if (isVerified.value){
        Column(Modifier.fillMaxSize().background(Color.Yellow)) {

        }
    }
}

@Composable
fun SetPasswordUI(
    passwordState: MutableState<TextFieldValue>,
    context: Context,
    masterKeyAlias: String,
    onPasswordSet: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = passwordState.value,
            onValueChange = {
                passwordState.value = it
            },
            label = { Text("Enter Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            EncryptedSharedPrefsManager.savePassword(context, passwordState.value.text, masterKeyAlias)
            onPasswordSet()
        }
        ) {
            Text("Set Password")
        }
    }
}

@Composable
fun EnterPasswordUI(
    context: Context,
    masterKeyAlias: String,
    onPasswordVerified: (Boolean) -> Unit
) {

    val passwordState = remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = passwordState.value,
            onValueChange = {
                passwordState.value = it
            },
            label = { Text("Enter Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val storedPassword = EncryptedSharedPrefsManager.getPassword(context, masterKeyAlias)
            onPasswordVerified(passwordState.value == storedPassword)
            Log.d("pass",storedPassword.toString())
        }) {
            Text("Unlock")
        }
    }
}

object EncryptedSharedPrefsManager {

    fun savePassword(context: Context, password: String, masterKeyAlias: String) {
        val sharedPreferences = EncryptedSharedPreferences.create(
            "secret_shared_prefs",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        sharedPreferences.edit().putString("password", password).apply()
    }

    fun getPassword(context: Context, masterKeyAlias: String): String? {
        val sharedPreferences = EncryptedSharedPreferences.create(
            "secret_shared_prefs",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        return sharedPreferences.getString("password", null)
    }

}



