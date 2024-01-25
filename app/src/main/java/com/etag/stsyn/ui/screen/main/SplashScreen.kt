package com.etag.stsyn.ui.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import com.etag.stsyn.R
import com.etag.stsyn.util.SPLASH_TIME_OUT
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onTimeOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentOnTimeOut by rememberUpdatedState(onTimeOut)

    // finish splash screen after 1 second
    LaunchedEffect(Unit) {
        delay(SPLASH_TIME_OUT)
        currentOnTimeOut()
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.img),
            modifier = Modifier.scale(1.7f),
            contentDescription = null
        )
    }
}