package com.etag.stsyn.util

import android.content.Context
import androidx.navigation.NavController

object AppUtil {
    fun getVersionCode(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (e: Exception) {
            "-1"
        }
    }

    fun logout(navController: NavController, loginScreen: String) {
        navController.navigate(loginScreen) {
            // Clear the entire back stack
            popUpTo(0) {
                inclusive = true
            }
        }
    }
}