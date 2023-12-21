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
import com.etag.stsyn.domain.model.TabOption
import com.etag.stsyn.ui.components.ConfirmationDialog
import com.etag.stsyn.ui.components.DisableBackPress
import com.etag.stsyn.ui.components.TabBarLayout
import com.etag.stsyn.util.DataSource
import com.etag.stsyn.util.OptionType
import com.etag.stsyn.util.TabUtil
import com.etag.stsyn.util.TransitionUtil
import kotlinx.coroutines.delay

@Composable
fun DetailScreen(
    navigateToHomeScreen: () -> Unit,
    optionType: OptionType,
    modifier: Modifier = Modifier
) {
    var showTabBar by remember { mutableStateOf(false) }
    var options by remember { mutableStateOf(listOf<TabOption>()) }

    LaunchedEffect(Unit) {
        delay(300)
        showTabBar = true
        options = TabUtil.getTabDetails(optionType)
    }

    DisableBackPress()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        var tabTitle by remember { mutableStateOf("") }
        var showConfirmationDialog by remember { mutableStateOf(false) }

        AnimatedVisibility(
            visible = showTabBar,
            enter = TransitionUtil.slideInVerticallyFromTop,
            exit = TransitionUtil.slideOutVerticallyToTop
        ) {

            ConfirmationDialog(
                showDialog = showConfirmationDialog,
                title = "Are you sure you want to exit?",
                cancelTitle = "No",
                confirmTitle = "Exit",
                onCancelClick = { showConfirmationDialog = false },
                onConfirmClick = {
                    showConfirmationDialog = false
                    navigateToHomeScreen()
                })

            TabBarLayout(options = options, selected = showConfirmationDialog, onTabSelected = {
                // check whether current tab item is exit tab
                if (it.equals(DataSource.tabOptions.get(DataSource.tabOptions.size - 1).title)) {
                    showConfirmationDialog = true
                }
            })
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = tabTitle)
        }
    }
}