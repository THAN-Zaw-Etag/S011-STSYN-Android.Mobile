package com.etag.stsyn.util

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavController
import com.etag.stsyn.enums.DeviceSize
import com.etag.stsyn.ui.navigation.Graph
import com.tzh.retrofit_module.enum.ItemStatus



object AppUtil {
    fun getVersionCode(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (e: Exception) {
            "-1"
        }
    }

    fun logout(navController: NavController) {
        navController.navigate(Graph.AUTHENTICATION) {
            // Clear the entire back stack
            popUpTo(0) {
                inclusive = true
            }
        }
    }


    @Composable
    fun getDeviceSize(): DeviceSize {
        val configuration = LocalConfiguration.current
        val screenWidthDp = configuration.screenWidthDp

        return when {
            screenWidthDp < MEDIUM_SCREEN_WIDTH_DP -> DeviceSize.SMALL
            screenWidthDp < LARGE_SCREEN_WIDTH_DP -> DeviceSize.MEDIUM
            else -> DeviceSize.TABLET
        }
    }

    fun baseUrlValidationStatus(newBaseUrl: String): Boolean {
        return !(!newBaseUrl.startsWith("http://") && !newBaseUrl.startsWith("https://"))
    }

}