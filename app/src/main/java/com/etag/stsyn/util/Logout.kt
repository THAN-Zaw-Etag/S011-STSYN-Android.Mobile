package com.etag.stsyn.util

import androidx.navigation.NavController


fun logout(navController: NavController, loginScreen: String) {
    navController.navigate(loginScreen) {
        // Clear the entire back stack
        popUpTo(0) {
            inclusive = true
        }
    }
}