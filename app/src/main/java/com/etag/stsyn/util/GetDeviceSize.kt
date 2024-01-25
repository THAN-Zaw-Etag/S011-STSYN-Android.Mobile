package com.etag.stsyn.util


import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration



private val SMALL_SCREEN_WIDTH_DP = 320
private val MEDIUM_SCREEN_WIDTH_DP = 480
private val LARGE_SCREEN_WIDTH_DP = 720

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
enum class DeviceSize {
    SMALL, MEDIUM, TABLET
}