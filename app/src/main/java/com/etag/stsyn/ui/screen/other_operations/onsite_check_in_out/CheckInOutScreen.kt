package com.etag.stsyn.ui.screen.other_operations.onsite_check_in_out

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ErrorText
import com.etag.stsyn.ui.components.ScanIconButton
import com.etag.stsyn.ui.components.ScannedItem

@Composable
fun CheckInOutScreen(
    modifier: Modifier = Modifier
) {
    var scannedItems by remember { mutableStateOf(listOf<String>()) }

    Column(modifier = modifier.fillMaxSize()) {
        ErrorText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = "Note: Issuer and receiver must be from the same flight"
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(10) {
                ScannedItem(
                    id = "1C342422",
                    isSwipeable = true,
                    name = "data link jumper cable".toUpperCase(),
                    onSwipeToDismiss = {})
            }
        }
        BottomButtonLayout(scannedItems.size)
    }
}

@Composable
fun BottomButtonLayout(
    scannedItemCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Clear")
        ScanIconButton(onScan = { /*TODO*/ })
        Text("Total: $scannedItemCount")
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun CheckInOutScreenPreview() {
    CheckInOutScreen()
}