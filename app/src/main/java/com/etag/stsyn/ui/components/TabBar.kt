@file:OptIn(ExperimentalFoundationApi::class)

package com.etag.stsyn.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.etag.stsyn.domain.model.TabIcon
import com.etag.stsyn.domain.model.TabOption
import com.etag.stsyn.ui.theme.Purple80

@Composable
fun TabBarLayout(
    selected: Boolean,
    onTabSelected: (String) -> Unit,
    options: List<TabOption>
) {
    var selectedIndex by remember { mutableStateOf(0) }
    var canBeSelected by remember { mutableStateOf(true) }

    LaunchedEffect(selected) {
        canBeSelected = selected
    }

    TabRow(selectedTabIndex = selectedIndex) {
        options.forEachIndexed { index, tabOption ->
            Tab(
                selected = (selectedIndex == index) && false,
                selectedContentColor = Purple80,
                icon = {
                    when (tabOption.icon) {
                        is TabIcon.Vector -> {
                            TabItemIcon(icon = tabOption.icon.iconVector, selected = selectedIndex == index)
                        }
                        is TabIcon.Resource -> {
                            TabItemIcon(icon = tabOption.icon.iconRes, selected = selectedIndex == index)
                        }
                    }
                },
                text = {
                    Text(
                        text = tabOption.title.uppercase(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = if (selectedIndex == index) Purple80 else Color.Gray
                    )
                },
                onClick = {
                    selectedIndex = index
                    onTabSelected(tabOption.title)
                })
        }
    }
}

@Composable
private fun TabItemIcon(
    icon: ImageVector,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = icon,
        modifier = modifier,
        tint = if (selected) MaterialTheme.colorScheme.primary else Color.Gray,
        contentDescription = null
    )
}

@Composable
private fun TabItemIcon(
    @DrawableRes icon: Int,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(id = icon),
        modifier = modifier,
        tint = if (selected) MaterialTheme.colorScheme.primary else Color.Gray,
        contentDescription = null
    )
}
