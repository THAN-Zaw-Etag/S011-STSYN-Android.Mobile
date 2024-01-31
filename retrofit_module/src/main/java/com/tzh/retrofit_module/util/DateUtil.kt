package com.tzh.retrofit_module.util

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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

    fun getCurrentDate(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")

        val formattedDateTime = currentDateTime.format(formatter)

        return formattedDateTime
    }
}