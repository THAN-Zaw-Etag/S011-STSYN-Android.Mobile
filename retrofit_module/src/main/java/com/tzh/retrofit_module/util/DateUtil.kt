package com.tzh.retrofit_module.util

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import com.tzh.retrofit_module.util.DateUtil.getCurrentDateTimeFormattedWithZone
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
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

        val formattedDateTime = currentDateTime.format(FORMATTER)

        return formattedDateTime
    }

    fun getCurrentDateTimeFormattedWithZone(): String {
        val currentDateTime = ZonedDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return currentDateTime.format(formatter)
    }


}

fun String.isBefore(date: String): Boolean {
    /*val date1 = LocalDateTime.parse(this, FORMATTER)
    val date2 = LocalDateTime.parse(date, FORMATTER)*/

    val comparison = this.compareTo(date)

    return comparison < 0
}

fun main() {
    println(getCurrentDateTimeFormattedWithZone())
}

