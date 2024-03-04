package com.etag.stsyn.ui.components

import android.content.Context
import androidx.activity.ComponentActivity

fun exitApp(context: Context) {
    val ctx = context as? ComponentActivity ?: return
    ctx.finish()
}