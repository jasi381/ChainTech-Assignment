package com.jasmeet.chaintechassignment.view

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jasmeet.chaintechassignment.view.screens.FingerPrintAuth
import com.jasmeet.chaintechassignment.view.screens.HomeScreen
import com.jasmeet.chaintechassignment.view.screens.Screens


@Composable
fun Navigator() {

    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Screens.AuthScreen.route) {
        composable(
            Screens.AuthScreen.route,
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() },
        ) {
            FingerPrintAuth(nav)
        }
        composable(
            Screens.HomeScreen.route,
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() },
        ) {
            HomeScreen()
        }
    }

}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition() =
    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(800))


private fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition() =
    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(800))