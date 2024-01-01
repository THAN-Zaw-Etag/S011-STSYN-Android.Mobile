package com.etag.stsyn.ui.screen.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.DetailScreenConfigurationGraphBuilder
import com.etag.stsyn.ui.components.ConfirmationDialog
import com.etag.stsyn.ui.components.DisableBackPress
import com.etag.stsyn.ui.components.TabBarLayout
import com.etag.stsyn.ui.viewmodel.SharedUiViewModel
import com.etag.stsyn.util.OptionType
import com.etag.stsyn.util.TabUtil
import com.etag.stsyn.util.TransitionUtil
import kotlinx.coroutines.delay

@Composable
fun DetailScreen(
    sharedUiViewModel: SharedUiViewModel,
    navigateToHomeScreen: () -> Unit,
    optionType: OptionType,
    modifier: Modifier = Modifier
) {
    var showTabBar by remember { mutableStateOf(false) }
    var options = TabUtil.getTabDetails(optionType)

    LaunchedEffect(Unit) {
        delay(300)
        showTabBar = true
    }

    DisableBackPress()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        var tabTitle by remember { mutableStateOf(options.get(0).title) }
        var showConfirmationDialog by remember { mutableStateOf(false) }
        var canBeSelected by remember { mutableStateOf(false) }
        var isSaved by remember { mutableStateOf(false) }

        AnimatedVisibility(
            visible = showTabBar,
            enter = TransitionUtil.slideInVerticallyFromTop,
            exit = TransitionUtil.slideOutVerticallyToTop
        ) {

            ConfirmationDialog(
                showDialog = showConfirmationDialog,
                title = if (isSaved) "Exit?" else "Exit without save?",
                cancelTitle = "Cancel",
                confirmTitle = "Exit",
                onCancelClick = {
                    showConfirmationDialog = false
                    canBeSelected = false
                },
                onConfirmClick = {
                    showConfirmationDialog = false
                    canBeSelected = true
                    navigateToHomeScreen()
                }
            )

            Column {
                TabBarLayout(
                    options = options,
                    selected = canBeSelected,
                    onTabSelected = {
                        tabTitle = it

                        // check whether current tab item is exit tab
                        if (it.equals(options.get(options.size - 1).title)) {
                            showConfirmationDialog = true
                        }
                    }
                )

                // To show when tab bar is visible
                DetailScreenConfigurationGraphBuilder.build(
                    optionType = optionType,
                    tabTitle = tabTitle,
                    sharedUiViewModel = sharedUiViewModel
                )
            }
        }
    }
}