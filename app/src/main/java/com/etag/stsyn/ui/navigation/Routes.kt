package com.etag.stsyn.ui.navigation

sealed class Routes(val name: String, val title: String = "") {
    object SplashScreen : Routes("/splash_screen")
    object LoginScreen : Routes("/main_screen")
    object LoginContentScreen : Routes("/login_screen")
    object HomeScreen : Routes("/home_screen", "Home")
    object HomeContentScreen : Routes("/home_content_screen")
    object BookOutScreen : Routes(name = "/book_out_screen", title = "Book Out")
    object BookInScreen : Routes(name = "/book_in_screen", title = "Book In")

    object SettingsScreen : Routes(name = "/settings_screen", title = "Settings")

    object DetailScreen : Routes(name = "/detail_screen")

    object OtherOperationsScreen :
        Routes(name = "/other_operations_screen", title = "Other Operations")
}