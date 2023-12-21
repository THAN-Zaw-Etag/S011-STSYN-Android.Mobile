package com.etag.stsyn.ui.screen.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.TabBarLayout
import com.etag.stsyn.util.DataSource
import com.etag.stsyn.util.TransitionUtil
import kotlinx.coroutines.delay

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier
) {
    var showTabBar by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        showTabBar = true
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        var tabTitle by remember {
            mutableStateOf("")
        }

        AnimatedVisibility(
            visible = showTabBar,
            enter = TransitionUtil.slideInVerticallyFromTop,
            exit = TransitionUtil.slideOutVerticallyToTop
        ) {
            TabBarLayout(options = DataSource.tabOptions, onTabSelected = {
                tabTitle = it
            })
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = tabTitle)
        }
    }
}