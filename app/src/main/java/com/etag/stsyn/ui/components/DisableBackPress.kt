package com.etag.stsyn.ui.components

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun DisableBackPress() {
    val context = LocalContext.current
    val onBackPressedCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Disable back press behavior (do nothing)
            }
        }
    }

    val backDispatcherOwner = context as? OnBackPressedDispatcherOwner
    val backDispatcher: OnBackPressedDispatcher? = backDispatcherOwner?.onBackPressedDispatcher

    if (backDispatcher != null) {
        DisposableEffect(key1 = backDispatcher) {
            backDispatcher.addCallback(onBackPressedCallback)
            onDispose {
                onBackPressedCallback.remove()
            }
        }
    }
}