package com.jasmeet.chaintechassignment.view

import android.content.Context
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jasmeet.chaintechassignment.model.auth.BiometricAuthenticator
import com.jasmeet.chaintechassignment.view.screens.HomeScreen
import com.jasmeet.chaintechassignment.view.screens.PassWordScreen3
import com.jasmeet.chaintechassignment.view.screens.Screens
import com.jasmeet.chaintechassignment.view.theme.ChainTechAssignmentTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(android.graphics.Color.TRANSPARENT,android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(android.graphics.Color.TRANSPARENT,android.graphics.Color.TRANSPARENT),
        )

        setContent {
            ChainTechAssignmentTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun Greeting(nav: NavHostController) {
    val activity = LocalContext.current as FragmentActivity
    val biometricAuthenticator = BiometricAuthenticator(activity)

    var message by remember {
        mutableStateOf("")
    }

    var id  by remember {
        mutableIntStateOf(0)
    }

    val context = LocalContext.current



    LaunchedEffect(key1 = true) {

        if(getBoolean("Verified",context)){
            nav.navigate(Screens.SuccessScreen.route)
        }
        else {
            biometricAuthenticator.promptBioMetricAuth(
                title = "Login",
                subTitle = "Use your finger print or face id",
                negativeButtonText = "Cancel",
                fragmentActivity = activity,
                onSuccess = {
                    message = "Success"
                    id = 1
                    saveBoolean("Verified", true, context)
                    nav.navigate(Screens.SuccessScreen.route)

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

    }
    Text(text = message)

    if (id == -1){
        TextButton(onClick = {
            biometricAuthenticator.promptBioMetricAuth(
                title = "Login",
                subTitle = "Use your finger print or face id",
                negativeButtonText = "Cancel",
                fragmentActivity = activity,
                onSuccess = {
                    message = "Success"
                    id = 1
                    nav.navigate(Screens.SuccessScreen.route)
                },
                onFailed = {
                    message = "Wrong Fingerprint or face Id"
                    id = -1
                },
                onError = {_,error ->
                    message = error
                    id = -1
                }
            )
        }) {
            Text(text = "Try Again")
        }
    }
}

@Composable
fun Navigator() {

    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Screens.AuthScreen.route ){
        composable(Screens.AuthScreen.route){
            Greeting( nav)
        }
        composable(Screens.SuccessScreen.route){
            val context = LocalContext.current
            Column(
                Modifier
                    .fillMaxSize()
                    .background(Color.Yellow)) {
                TextButton(onClick = {
                    saveBoolean("Verified", false, context)


                     }) {
                    Text(text = "Hello world")
                }


            }
        }
    }

}


fun saveBoolean(key: String, value: Boolean, context: Context) {
    val sharedPreferences = context.getSharedPreferences(key,Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean(key, value)
    editor.apply()
}

fun getBoolean(key: String, context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean(key, false) // or true, depending on your needs
}


