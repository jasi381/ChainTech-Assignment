package com.jasmeet.chaintechassignment.view.screens

sealed class Screens (val route :String){
    data object AuthScreen : Screens("auth")
    data object SuccessScreen : Screens("auth1")
}