@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen.book_in.book_in_cal

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.screen.base.BaseSaveScreen
import com.tzh.retrofit_module.util.DateUtil

@Composable
fun BookInCalSaveScreen(
    bookInCalViewModel: BookInCalViewModel,
    modifier: Modifier = Modifier
) {
    val rfidUiState by bookInCalViewModel.rfidUiState.collectAsStateWithLifecycle()
    var selectedDate by remember { mutableStateOf(DateUtil.getCurrentFormattedDate()) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    BaseSaveScreen(isError = false, onSave = {  }) {
        SaveItemLayout(icon = Icons.Default.Person, itemTitle = "User") {
            Text(text = "Admin-123S")
        }
        SaveItemLayout(
            icon = Icons.Default.CalendarMonth,
            itemTitle = "Next Calibration Date",
            modifier = Modifier.clickable {
                showDatePicker = true
            }) {
            Text(text = selectedDate)
        }
    }

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