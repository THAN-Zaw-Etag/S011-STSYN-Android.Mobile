package com.etag.stsyn.util

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import java.util.Locale

object DateUtil {
    fun getCurrentFormattedDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault())
        return dateFormat.format(calendar)
    }

    fun convertMillisToFormattedDate(millis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis

        val dateFormat = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}