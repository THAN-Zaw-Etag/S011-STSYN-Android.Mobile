package com.etag.stsyn.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource

sealed class CustomIcon {
    data class Resource(val iconRes: Int) : CustomIcon()
    data class Vector(val iconVector: ImageVector) : CustomIcon()
}

@Composable
fun StsynIcon(
    icon: CustomIcon,
    color: Color,
    modifier: Modifier = Modifier
) {
    when (icon) {
        is CustomIcon.Resource -> Icon(
            painter = painterResource(id = icon.iconRes),
            tint = color,
            contentDescription = null,
            modifier = modifier
        )

        is CustomIcon.Vector -> Icon(
            imageVector = icon.iconVector,
            tint = color,
            contentDescription = null,
            modifier = modifier
        )
    }
}

@Composable
fun StsynIcon(
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Icon(imageVector = icon, tint = color, contentDescription = null, modifier = modifier)
}

@Composable
fun StsynIcon(
    @DrawableRes icon: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(id = icon),
        tint = color,
        contentDescription = null,
        modifier = modifier
    )
}