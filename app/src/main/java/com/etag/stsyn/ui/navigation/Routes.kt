package com.etag.stsyn.ui.navigation

sealed class Routes(val name: String, val title: String = "") {
    data object SplashScreen : Routes("/splash_screen")
    data object LoginScreen : Routes("/main_screen")
    data object LoginContentScreen : Routes("/login_screen")
    data object HomeScreen : Routes("/home_screen", "Home")
    data object HomeContentScreen : Routes("/home_content_screen")
    data object BookOutScreen : Routes(name = "/book_out_screen", title = "Book Out")
    data object BookInScreen : Routes(name = "/book_in_screen", title = "Book In")

    data object SettingsScreen : Routes(name = "/settings_screen", title = "Settings")

    data object DetailScreen : Routes(name = "/detail_screen")

    data object OtherOperationsScreen :
        Routes(name = "/other_operations_screen", title = "Other Operations")
}