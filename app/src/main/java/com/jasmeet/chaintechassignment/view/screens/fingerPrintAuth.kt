package com.jasmeet.chaintechassignment.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import com.jasmeet.chaintechassignment.model.auth.BiometricAuthenticator
import com.jasmeet.chaintechassignment.view.appComponents.TextComponent

@Composable
fun FingerPrintAuth(nav: NavHostController) {
    val activity = LocalContext.current as FragmentActivity
    val biometricAuthenticator = BiometricAuthenticator(activity)

    var message by remember {
        mutableStateOf("")
    }

    var id by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(key1 = true) {
        biometricAuthenticator.promptBioMetricAuth(
            title = "Login",
            subTitle = "Use your finger print or face id",
            negativeButtonText = "Cancel",
            fragmentActivity = activity,
            onSuccess = {
                message = "Success"
                id = 1
                nav.navigate(Screens.HomeScreen.route)

            },
            onFailed = {
                message = "Wrong Fingerprint or face Id"
                id = -1
            },
            onError = { _, error ->
                message = error
                id = -1
            }
        )


    }


    if (id == -1) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.7f)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
                onClick = {
                    biometricAuthenticator.promptBioMetricAuth(
                        title = "Login",
                        subTitle = "Use your finger print or face id",
                        negativeButtonText = "Cancel",
                        fragmentActivity = activity,
                        onSuccess = {
                            message = "Success"
                            id = 1
                            nav.navigate(Screens.HomeScreen.route)
                        },
                        onFailed = {
                            message = "Wrong Fingerprint or face Id"
                            id = -1
                        },
                        onError = { _, error ->
                            message = error
                            id = -1
                        }
                    )
                }) {
                TextComponent(
                    text = "Try Again",
                    textColor = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            TextComponent(text = "Current Status : $message", textColor = Color.White)

        }

    }
}