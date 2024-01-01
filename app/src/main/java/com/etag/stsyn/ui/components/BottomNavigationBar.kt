package com.etag.stsyn.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.etag.stsyn.ui.navigation.Routes
import com.etag.stsyn.ui.theme.Purple80
import com.etag.stsyn.util.DataSource
import com.etag.stsyn.util.TransitionUtil

@Composable
fun BottomNavigationBar(
    selectedItem: String,
    showBottomBar: Boolean,
    onBottomNavigationItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var show by remember { mutableStateOf(false) }
    var selectedBottomNavigationItem by remember { mutableStateOf(Routes.HomeScreen.title) }

    LaunchedEffect(showBottomBar, selectedItem) {
        show = showBottomBar
        selectedBottomNavigationItem = selectedItem
    }

    AnimatedVisibility(
        visible = show,
        enter = TransitionUtil.slideInVertically,
        exit = TransitionUtil.slideOutVertically
    ) {
        BottomAppBar(modifier = modifier) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                DataSource.bottomNavigationItems.forEach {
                    CustomNavigationItem(
                        selected = selectedBottomNavigationItem == it.route,
                        onClick = {
                            selectedBottomNavigationItem = it.route
                            onBottomNavigationItemSelected(it.route)
                        },
                        title = it.title,
                        icon = it.icon,
                        modifier = Modifier
                            .weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun CustomNavigationItem(
    selected: Boolean,
    title: String,
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            //.padding(8.dp)
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            tint = if (selected) Purple80 else Color.Gray,
            modifier = Modifier.size(24.dp),
            contentDescription = null
        )
        Text(
            text = title,
            maxLines = 1,
            fontSize = 14.sp,
            color = if (selected) Purple80 else Color.Gray,
            overflow = TextOverflow.Ellipsis
        )
    }
}