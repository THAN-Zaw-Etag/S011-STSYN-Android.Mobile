package com.etag.stsyn.ui.navigation

sealed class Routes(val name: String) {
    object SplashScreen : Routes("/splash_screen")
    object MainScreen : Routes("/main_screen")
    object LoginScreen : Routes("/login_screen")
    object HomeScreen : Routes("/home_screen")
}