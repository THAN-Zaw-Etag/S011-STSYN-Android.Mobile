package com.etag.stsyn.ui.screen.other_operations.det_p_loan

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonPin
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
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.theme.Purple80

@Composable
fun OtherDetPLoanOutSaveScreen(
    modifier: Modifier = Modifier
) {
    var value by remember { mutableStateOf("") }
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SaveItemLayout(icon = Icons.Default.Person, itemTitle = "Issuer") {
            Text(text = "Admin - 123S")
        }
        SaveItemLayout(icon = Icons.Default.Person, itemTitle = "Receiver (Key in)") {
            TextField(
                value = value,
                onValueChange = { value = it },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        Purple80,
                        RoundedCornerShape(5.dp)
                    )
            )
        }
        SaveItemLayout(
            icon = Icons.Default.PersonPin,
            itemTitle = "Higher Authority",
            showRefreshIcon = true
        ) {
            Text(text = "-")
        }
    }
}