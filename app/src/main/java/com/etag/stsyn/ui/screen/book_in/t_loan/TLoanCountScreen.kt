package com.etag.stsyn.ui.screen.book_in.t_loan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.SegmentedControl
import com.etag.stsyn.ui.components.TLoanScannedItem

@Composable
fun TLoanCountScreen(
    items: List<String>
) {
    Column(modifier = Modifier.padding(16.dp)) {

        SegmentedControl(onTabSelected = {})
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(items) {
                TLoanScannedItem()
            }
        }
    }

}