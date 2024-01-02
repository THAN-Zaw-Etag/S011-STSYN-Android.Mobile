package com.etag.stsyn.ui.screen.book_out.book_out_box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.etag.stsyn.ui.components.DropDown
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.screen.base.BaseSaveScreen
import com.etag.stsyn.util.Purpose

@Composable
fun BookOutBoxSaveScreen(
    bookOutBoxViewModel: BookOutBoxViewModel,
    modifier: Modifier = Modifier
) {
    BaseSaveScreen(isError = false, onSave = { }) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            SaveItemLayout(icon = Icons.Default.Person, itemTitle = "User") {
                Text(text = "Admin - 123S")
            }
            SaveItemLayout(icon = Icons.Default.TrackChanges, itemTitle = "Purpose") {
                DropDown(
                    items = Purpose.values().toList().map { it.toString() },
                    defaultValue = "Choose a purpose",
                    onSelected = { item ->

                    }
                )
            }
        }
    }
}