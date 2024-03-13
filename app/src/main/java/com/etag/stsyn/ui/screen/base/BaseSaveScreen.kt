package com.etag.stsyn.ui.screen.base

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.ErrorText
import com.etag.stsyn.ui.components.SaveButton
import com.etag.stsyn.ui.components.ScanIconButton

@Composable
fun BaseSaveScreen(
    modifier: Modifier = Modifier,
    isError: Boolean,
    isUsCase: Boolean = false,
    errorMessage: String = "",
    onSave: () -> Unit,
    onScan: () -> Unit = {},
    content: @Composable () -> Unit
) {
    var error by remember { mutableStateOf(false) }

    LaunchedEffect(isError) {
        Log.d(TAG, "BaseSaveScreen: $isError")
        error = isError
    }

    Column(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f)) {
            content()
        }
        if (error) {
            ErrorText(text = errorMessage)
            Spacer(modifier = Modifier.height(16.dp))
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isUsCase) ScanIconButton(onScan = onScan)
                else SaveButton(onClick = onSave)
            }
        }
    }
}