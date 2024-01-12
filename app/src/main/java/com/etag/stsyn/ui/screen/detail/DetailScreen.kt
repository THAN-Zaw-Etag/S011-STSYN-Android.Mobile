@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.etag.stsyn.ui.screen.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.ReaderLifeCycle
import com.etag.stsyn.ui.components.ConfirmationDialog
import com.etag.stsyn.ui.components.DisableBackPress
import com.etag.stsyn.ui.components.TabBarLayout
import com.etag.stsyn.util.OptionType
import com.etag.stsyn.util.TabUtil
import com.etag.stsyn.util.TransitionUtil
import com.etag.stsyn.util.getScreensByOptionType
import com.etag.stsyn.util.getViewModelByOptionType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    navigateToHomeScreen: () -> Unit,
    optionType: OptionType,
    modifier: Modifier = Modifier
) {
    var showTabBar by remember { mutableStateOf(false) }
    var options = TabUtil.getTabDetails(optionType)
    var exitTitle = options.get(options.size - 1).title
    var tabTitle by remember { mutableStateOf(options.get(0).title) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var oldSelectedIndex by remember { mutableStateOf(0) }
    var canBeSelected by remember { mutableStateOf(false) }
    var isSaved by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState { options.size }
    val scope = rememberCoroutineScope()

    val viewModel = getViewModelByOptionType(optionType = optionType)
    val screens = getScreensByOptionType(optionType = optionType, viewModel = viewModel)

    ReaderLifeCycle(viewModel = viewModel)

    LaunchedEffect(pagerState.currentPage) {
        val option = options.get(pagerState.currentPage)
        tabTitle = option.title
        if (option.title.equals(exitTitle)) showConfirmationDialog = true
    }

    LaunchedEffect(Unit) {
        delay(300)
        showTabBar = true
    }

    DisableBackPress()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        AnimatedVisibility(
            visible = showTabBar,
            enter = TransitionUtil.slideInVerticallyFromTop,
            exit = TransitionUtil.slideOutVerticallyToTop
        ) {

            ConfirmationDialog(showDialog = showConfirmationDialog,
                title = if (isSaved) "Exit?" else "Exit without save?",
                cancelTitle = "Cancel",
                confirmTitle = "Exit",
                onCancelClick = {
                    showConfirmationDialog = false
                    scope.launch { pagerState.animateScrollToPage(options.size - 2) }
                    canBeSelected = false
                },
                onConfirmClick = {
                    showConfirmationDialog = false
                    canBeSelected = true
                    navigateToHomeScreen()
                })

            Column {
                TabBarLayout(
                    pagerState = pagerState,
                    options = screens,
                    selected = canBeSelected,
                    oldSelectedIndex = oldSelectedIndex,
                    onTabSelected = { title, index ->
                        tabTitle = title

                        if (canBeSelected) oldSelectedIndex = index
                        // check whether current tab item is exit tab
                        if (title.equals(options.get(options.size - 1).title)) {
                            showConfirmationDialog = true
                        }
                    })

                HorizontalPager(state = pagerState) {
                    // check current screen is scan screen or not
                    if (it != 0) {
                        viewModel.updateIsScanningStatus(false)
                        viewModel.stopScan()
                        viewModel.disableScan()
                    } else viewModel.enableScan()

                    val screen = screens.getOrNull(it)
                    if (screen != null) {
                        screen.screen?.invoke()
                    } else {
                        // If the screen is null, display a default screen or handle the scenario
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No screen available")
                        }
                    }
                }
            }
        }
    }
}