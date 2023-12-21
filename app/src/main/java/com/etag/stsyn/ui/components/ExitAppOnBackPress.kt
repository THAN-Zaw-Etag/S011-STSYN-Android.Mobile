package com.etag.stsyn.ui.components

import android.content.Context
import androidx.activity.ComponentActivity


fun ExitAppOnBackPress(context: Context) {
    val context = context as? ComponentActivity ?: return
    context.finish()
}