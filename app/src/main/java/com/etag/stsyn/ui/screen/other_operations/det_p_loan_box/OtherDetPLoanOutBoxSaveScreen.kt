package com.etag.stsyn.ui.screen.other_operations.det_p_loan_box

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person3
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
import com.etag.stsyn.ui.screen.base.BaseSaveScreen
import com.etag.stsyn.ui.theme.Purple80

@Composable
fun OtherDetPLoanOutBoxSaveScreen() {
    var inputValue by remember { mutableStateOf("") }

    BaseSaveScreen(isError = false, onSave = { /*TODO*/ }) {
        SaveItemLayout(icon = Icons.Default.Person, itemTitle = "Issuer") {
            Text(text = "Admin - 123S")
        }
        SaveItemLayout(icon = Icons.Default.Person, itemTitle = "Receiver (Key in)") {
            TextField(
                value = inputValue,
                onValueChange = { inputValue = it },
                modifier = Modifier.border(
                    1.dp,
                    Purple80,
                    RoundedCornerShape(5.dp)
                ),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                )
            )
        }

        SaveItemLayout(
            icon = Icons.Default.Person3,
            itemTitle = "Higher Authority",
            showRefreshIcon = true
        ) {
            Text(text = "-")
        }
    }

}