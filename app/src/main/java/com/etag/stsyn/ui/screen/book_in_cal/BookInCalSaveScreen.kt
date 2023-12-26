@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen.book_in_cal

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.screen.book_in.BookInSaveScreen
import com.etag.stsyn.util.DateUtil

@Composable
fun BookInCalSaveScreen() {
    var selectedDate by remember { mutableStateOf(DateUtil.getCurrentFormattedDate()) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    BookInSaveScreen(showSaveButton = false, onSave = {}, content = {
        SaveItemLayout(
            icon = Icons.Default.CalendarMonth,
            itemTitle = "Next Calibration Date",
            modifier = Modifier.clickable {
                showDatePicker = true
            }) {
            Text(text = selectedDate)
        }
    })

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDate =
                        DateUtil.convertMillisToFormattedDate(datePickerState.selectedDateMillis!!)
                    showDatePicker = false
                }) {
                    Text(text = "Done")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDatePicker = false
                }) {
                    Text(text = "Cancel")
                }
            }) {
            DatePicker(state = datePickerState)
        }
    }
}