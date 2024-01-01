package com.etag.stsyn.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class TabOption(
    val title: String,
    val icon: TabIcon,
)

sealed class TabIcon {
    data class Resource(val iconRes: Int): TabIcon()
    data class Vector (val iconVector: ImageVector) : TabIcon()
}