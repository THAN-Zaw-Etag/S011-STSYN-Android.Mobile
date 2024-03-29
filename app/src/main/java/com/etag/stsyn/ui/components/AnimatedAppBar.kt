@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.etag.stsyn.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.theme.Purple80
import com.etag.stsyn.util.TransitionUtil

@Composable
fun AnimatedAppBar(
    visible: Boolean,
    userName: String,
    title: String,
    icon: ImageVector,
    onIconClick: () -> Unit,
) {

    var startChar by remember { mutableStateOf("") }

    LaunchedEffect(userName) {
        startChar = if (userName.isNotEmpty()) userName[0].toString() else ""
    }

    AnimatedVisibility(
        visible = visible,
        enter = TransitionUtil.slideInVerticallyFromTop,
        exit = TransitionUtil.slideOutVerticallyToTop
    ) {
        TopAppBar(
            title = { Text(text = title, modifier = Modifier.padding(horizontal = 8.dp)) },
            navigationIcon = {
                IconButton(onClick = onIconClick) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            },
            actions = {
                UserNameShortcutIcon(name = startChar)
            }
        )
    }
}

@Composable
fun UserNameShortcutIcon(
    name: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Purple80)
    ) {
        Text(
            text = if (name.isNotEmpty()) name[0].toString() else name,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
