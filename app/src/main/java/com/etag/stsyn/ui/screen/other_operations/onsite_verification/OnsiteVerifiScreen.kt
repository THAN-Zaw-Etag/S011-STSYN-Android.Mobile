@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen.other_operations.onsite_verification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ConfirmationDialog
import com.etag.stsyn.ui.components.DetailBottomSheetScaffold
import com.etag.stsyn.ui.components.ScanIconButton
import com.etag.stsyn.ui.components.ScannedItem
import com.etag.stsyn.ui.screen.BottomSheetContent
import com.etag.stsyn.ui.theme.Purple80
import kotlinx.coroutines.launch

@Composable
fun OnsiteVerifyScreen() {
    val hasScanned by remember { mutableStateOf(true) }
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            skipPartiallyExpanded = false,
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )
    val coroutineScope = rememberCoroutineScope()

    DetailBottomSheetScaffold(
        state = scaffoldState,
        sheetContent = { BottomSheetContent(itemList = listOf()) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ScannedBoxSection(id = "", description = "")
            if (hasScanned) {
                ScannedContent(onReset = {}, modifier = Modifier.weight(1f), onItemClick = {
                    if (scaffoldState.bottomSheetState.isVisible) coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
                    else coroutineScope.launch { scaffoldState.bottomSheetState.expand() }
                })
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Os: 2", color = if (hasScanned) Color.Black else Color.Transparent)
                ScanIconButton(onScan = { /*TODO*/ })
                Text(text = "Done: 0", color = if (hasScanned) Color.Black else Color.Transparent)
            }
        }
    }
}

@Composable
private fun ScannedContent(
    // pass item list here
    onReset: () -> Unit,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showResetDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        ConfirmationDialog(
            showDialog = showResetDialog,
            title = "Reset",
            cancelTitle = "Cancel",
            confirmTitle = "Reset",
            onCancelClick = { showResetDialog = false },
            onConfirmClick = {
                showResetDialog = false
                onReset()
            })

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "Item booked out (2)",
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Reset",
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {

                })
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(5) {
                ScannedItem(
                    id = "sn000021",
                    name = "data link jumper cable",
                    showTrailingIcon = true,
                    onItemClick = onItemClick
                )
            }
        }
    }
}

@Composable
fun ScannedBoxSection(
    id: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Purple80.copy(0.1f))
            .padding(16.dp)
    ) {
        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.DarkGray)) {
                append("Item Description: ")
            }
            withStyle(style = SpanStyle(color = Color.Black)) {
                append(if (description.isEmpty()) "-" else description.toUpperCase())
            }
        })

        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.DarkGray)) {
                append("ID: ")
            }
            withStyle(style = SpanStyle(color = Color.Black)) {
                append(if (id.isEmpty()) "-" else id.toUpperCase())
            }
        })
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun OnsiteVerifyScreenPreview() {
    OnsiteVerifyScreen()
}