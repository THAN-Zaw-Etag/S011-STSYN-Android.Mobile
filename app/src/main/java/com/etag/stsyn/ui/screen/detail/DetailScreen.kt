package com.etag.stsyn.ui.screen.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.etag.stsyn.ui.components.TabBarLayout
import com.etag.stsyn.util.DataSource

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TabBarLayout(options = DataSource.tabOptions)
    }
}