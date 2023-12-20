package com.etag.stsyn.util

import com.etag.stsyn.R

object BatteryImageUtil {
    fun getBatteryImageByPercentage(percentage: Int): Int {
        return when (percentage) {
            in 0..20 -> R.drawable.battery_very_low
            in 21..40 -> R.drawable.battery_low
            in 41..60 -> R.drawable.battery_half
            in 61..80 -> R.drawable.battery_good
            in 81..100 -> R.drawable.battery_full
            else -> R.drawable.battery_very_low
        }
    }
}