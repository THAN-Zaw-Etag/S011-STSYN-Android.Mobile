package com.tzh.retrofit_module.util

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.Log
import com.tzh.retrofit_module.enum.ItemStatus
import java.time.Instant
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
        return currentDateTime.format(FORMATTER)
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
    val date1 =  Instant.parse(this+"Z")
    val date2 = Instant.parse(date+"Z")
     val comparison = this.compareTo(date)
    return comparison < 0
    //return date1.isBefore(date2)
}


/*
fun main() {
    val date1 = Instant.parse("2024-02-05T16:10:00.000Z")
    val date2 = Instant.parse("2024-03-13T16:18:09.453Z")

    if (date1.isBefore(date2)) {
        println("Date 1 is before Date 2.")
    } else if (date2.isBefore(date1)) {
        println("Date 2 is before Date 1.")
    } else {
        println("Both dates are equal.")
    }
}*/
