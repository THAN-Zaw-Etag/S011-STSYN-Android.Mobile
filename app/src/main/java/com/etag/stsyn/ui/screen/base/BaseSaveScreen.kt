package com.etag.stsyn.ui.screen.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ErrorText
import com.etag.stsyn.ui.components.SaveButton
import com.etag.stsyn.ui.components.ScanIconButton

@Composable
fun BaseSaveScreen(
    isError: Boolean,
    isUsCase: Boolean = false,
    errorMessage: String = "",
    modifier: Modifier = Modifier,
    onSave: () -> Unit,
    onScan: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f)) {
            content()
        }
        if (isError) {
            ErrorText(text = errorMessage)
            Spacer(modifier = Modifier.height(16.dp))
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (true) ScanIconButton(onScan = onScan)
                else SaveButton(onClick = onSave)
            }
        }
    }
}