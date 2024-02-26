package com.tzh.retrofit_module.util

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import com.tzh.retrofit_module.util.DateUtil.currentTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
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

    fun isUnderCalibrationAlert(calDate: String): Boolean {
        return try {
            val currentDate = getCurrentDate()
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            val date1 = LocalDate.parse(calDate, formatter)
            val date2 = LocalDate.parse(currentDate, formatter)

            val differences = ChronoUnit.DAYS.between(date1,date2)

            differences <= CALIBRATION_ALERT_DAYS
        } catch (e: Exception) {
            false
        }
    }

    fun getCurrentDateTimeFormattedWithZone(): String {
        val currentDateTime = ZonedDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return currentDateTime.format(formatter)
    }

    fun currentTime(): String {
        val currentTimerMillis = System.currentTimeMillis()
        val dateFormat =
            java.text.SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        return dateFormat.format(Date(currentTimerMillis))
    }

}

fun String.isBefore(date: String): Boolean {
    val comparison = this.compareTo(date)
    return comparison < 0
}

fun main() {
    //println(isUnderCalibrationAlert("2025-11-05T00:00:00"))
}

