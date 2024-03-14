package com.etag.stsyn.ui.screen.other_operations.det_p_loan_box

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.etag.stsyn.ui.components.STSYNTExtField
import com.etag.stsyn.ui.components.SaveItemLayout
import com.etag.stsyn.ui.screen.base.BaseSaveScreen
import com.etag.stsyn.ui.theme.Purple80

@Composable
fun OtherDetPLoanOutBoxSaveScreen(
    otherDetPLoanBoxViewModel: OtherDetPLoanBoxViewModel,
    modifier: Modifier = Modifier
) {
    var inputValue by remember { mutableStateOf("") }
    val rfidUiState by otherDetPLoanBoxViewModel.rfidUiState.collectAsStateWithLifecycle()

    BaseSaveScreen(isError = false, modifier = modifier, onSave = {  }) {
        SaveItemLayout(icon = Icons.Default.Person, itemTitle = "Issuer") {
            Text(text = "Admin - 123S")
        }
        SaveItemLayout(icon = Icons.Default.Person, itemTitle = "Receiver (Key in)") {
            STSYNTExtField(
                value = inputValue,
                label = {},
                onValueChange = {
                    inputValue = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp, color = Purple80, shape = RoundedCornerShape(5.dp)
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

        SaveItemLayout(
            icon = Icons.Default.Person3,
            itemTitle = "Higher Authority",
            showRefreshIcon = true
        ) {
            Text(text = "-")
        }
    }

}