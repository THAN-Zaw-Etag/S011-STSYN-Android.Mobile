package com.etag.stsyn.ui.screen.other_operations.t_loan_out

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.components.ScanIconButton
import com.etag.stsyn.ui.theme.Purple80

@Composable
fun TLoanOutSaveScreen(
    modifier: Modifier = Modifier
) {
    var inputValue by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f)) {
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
        ScanIconButton(
            onScan = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )
    }
}