package com.etag.stsyn.ui.navigation

sealed class Routes(val name: String, val title: String = "") {
    object SplashScreen : Routes("/splash_screen")
    object MainScreen : Routes("/main_screen")
    object LoginScreen : Routes("/login_screen")
    object HomeScreen : Routes("/home_screen", "Home")
    object HomeContentScreen : Routes("/home_content_screen")
    object BookOutScreen : Routes(name = "/book_out_screen", title = "Book Out")
    object BookOutBoxScreen : Routes(name = "/book_out_box_screen", title = "Book Out (Box)")
    object BookInScreen : Routes(name = "/book_in_screen", title = "Book In")
    object BookInBoxScreen : Routes(name = "/book_in_box_screen", title = "Book In (Box)")
    object BookInCalibrationScreen :
        Routes(name = "/book_in_calibration_screen", title = "Book In (Calibration)")

    object DetailScreen : Routes(name = "/detail_screen")

    object AcctCheckScreen : Routes(name = "/act_check_screen", title = "Act Check")
    object OtherOperationsScreen :
        Routes(name = "/other_operations_screen", title = "Other Operations")
}