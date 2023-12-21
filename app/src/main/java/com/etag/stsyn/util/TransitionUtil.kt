package com.etag.stsyn.util

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically

object TransitionUtil {
    val slideInVertically = fadeIn() + slideInVertically(animationSpec = tween(200),
        initialOffsetY = { 100 })
    val slideOutVertically = fadeOut() + slideOutVertically(animationSpec = tween(200),
        targetOffsetY = { 100 })

    val slideInVerticallyFromTop = fadeIn() + slideInVertically(animationSpec = tween(200),
        initialOffsetY = { -100 })
    val slideOutVerticallyToTop = fadeOut() + slideOutVertically(animationSpec = tween(200),
        targetOffsetY = { -100 })
}