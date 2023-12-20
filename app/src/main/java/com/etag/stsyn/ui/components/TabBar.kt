@file:OptIn(ExperimentalFoundationApi::class)

package com.etag.stsyn.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.etag.stsyn.domain.model.TabOption
import com.etag.stsyn.ui.theme.Purple80
import com.etag.stsyn.util.DataSource

@Composable
fun TabBarLayout(
    options: List<TabOption>, modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { options.size })

    var selectedIndex by remember { mutableStateOf(0) }
    TabRow(selectedTabIndex = selectedIndex) {
        options.forEachIndexed { index, tabOption ->
            Tab(selected = selectedIndex == index, selectedContentColor = Purple80, icon = {
                Icon(
                    imageVector = tabOption.icon,
                    tint = if (selectedIndex == index) Purple80 else Color.Gray,
                    contentDescription = null
                )
            }, text = {
                Text(
                    text = tabOption.title,
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = if (selectedIndex == index) Purple80 else Color.Gray
                )
            }, onClick = { selectedIndex = index })
        }
    }
}

@Composable
@Preview(showBackground = true)
fun TabBarLayoutPreview() {
    TabBarLayout(options = DataSource.tabOptions)
}