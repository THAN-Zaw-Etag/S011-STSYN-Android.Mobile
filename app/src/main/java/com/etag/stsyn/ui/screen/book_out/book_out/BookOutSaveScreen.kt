package com.etag.stsyn.ui.screen.book_out.book_out

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.DropDown
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.screen.base.BaseSaveScreen
import com.etag.stsyn.ui.theme.Purple80
import com.etag.stsyn.util.Purpose

@Composable
fun BookOutSaveScreen(
    modifier: Modifier = Modifier
) {
    var location by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    BaseSaveScreen(
        isError = isError,
        errorMessage = "Include Overdue Calibration Item, Only Can Book Out For Calibration.",
        onSave = { /*TODO*/ }) {

        Column(
            modifier = modifier.fillMaxSize()
        ) {
            SaveItemLayout(
                icon = Icons.Default.Person,
                itemTitle = "User"
            ) { Text(text = "Admin - 123S") }

            SaveItemLayout(icon = Icons.Outlined.TrackChanges, itemTitle = "Purpose") {
                DropDown(
                    items = Purpose.values().toList().map { it.toString() },
                    defaultValue = "Choose a purpose",
                    onSelected = { item ->

                    }
                )
            }

            SaveItemLayout(icon = Icons.Default.AddLocation, itemTitle = "Location") {
                TextField(
                    value = location,
                    onValueChange = { location = it },
                    modifier = Modifier.border(
                        width = 1.dp,
                        color = Purple80,
                        shape = RoundedCornerShape(5.dp)
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                )
            }
        }
    }

}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun BookOutSaveScreenPreview() {
    BookOutSaveScreen()
}