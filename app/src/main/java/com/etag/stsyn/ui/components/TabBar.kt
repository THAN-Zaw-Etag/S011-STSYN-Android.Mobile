@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.etag.stsyn.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.etag.stsyn.util.datasource.TabScreen
import kotlinx.coroutines.launch

@Composable
fun TabBarLayout(
    pagerState: PagerState,
    oldSelectedIndex: Int,
    options: List<TabScreen>,
    selected: Boolean, onTabSelected: (String, Int) -> Unit
) {
    var currentSelectIndex by remember { mutableStateOf(0) }
    var canBeSelected by remember { mutableStateOf(true) }
    var oldIndex by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(selected) {
        canBeSelected = selected
    }

    LaunchedEffect(oldSelectedIndex) {
        oldIndex = oldSelectedIndex
    }

    TabRow(selectedTabIndex = pagerState.currentPage) {
        options.forEachIndexed { index, tabOption ->
            Tab(
                selected = pagerState.currentPage == index,
                selectedContentColor = MaterialTheme.colorScheme.primary,
                icon = {
                    TabItemIcon(icon = tabOption.icon, selected = pagerState.currentPage == index)
                    /*when (tabOption.icon) {
                        is CustomIcon.Vector -> {
                            TabItemIcon(
                                icon = tabOption.icon.iconVector,
                                selected = pagerState.currentPage == index
                            )
                        }

                        is CustomIcon.Resource -> {
                            TabItemIcon(
                                icon = tabOption.icon.iconRes,
                                selected = pagerState.currentPage == index
                            )
                        }
                    }*/
                },
                text = {
                    Text(
                        text = tabOption.title.uppercase(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                },
                onClick = {
                    scope.launch { pagerState.scrollToPage(index) }
                    onTabSelected(tabOption.title, currentSelectIndex)
                })
        }
    }
}

@Composable
private fun TabItemIcon(
    icon: CustomIcon, selected: Boolean, modifier: Modifier = Modifier
) {
    StsynIcon(
        icon = icon,
        color = if (selected) MaterialTheme.colorScheme.primary else Color.Gray,
        modifier = modifier,
    )
}

@Composable
private fun TabItemIcon(
    @DrawableRes icon: Int, selected: Boolean, modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(id = icon),
        modifier = modifier,
        tint = if (selected) MaterialTheme.colorScheme.primary else Color.Gray,
        contentDescription = null
    )
}
