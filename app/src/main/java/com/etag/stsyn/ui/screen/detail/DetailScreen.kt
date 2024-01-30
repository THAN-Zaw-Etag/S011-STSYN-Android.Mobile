@file:OptIn(
    ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class
)

package com.etag.stsyn.ui.screen.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.ReaderLifeCycle
import com.etag.stsyn.enums.OptionType
import com.etag.stsyn.ui.components.AuthorizationTokenExpiredDialog
import com.etag.stsyn.ui.components.ConfirmationDialog
import com.etag.stsyn.ui.components.CustomIcon
import com.etag.stsyn.ui.components.DisableBackPress
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.TabBarLayout
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.util.TabUtil
import com.etag.stsyn.util.TransitionUtil
import com.etag.stsyn.util.datasource.getScreensByOptionType
import com.etag.stsyn.util.datasource.getViewModelByOptionType
import com.tzh.retrofit_module.util.AUTHORIZATION_FAILED_MESSAGE
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    isConnected: Boolean,
    optionType: OptionType,
    modifier: Modifier = Modifier,
    logOut: () -> Unit,
    navigateToHomeScreen: () -> Unit,
) {
    var showTabBar by remember { mutableStateOf(false) }
    var options = TabUtil.getTabDetails(optionType)
    var exitTitle = options.get(options.size - 1).title
    var tabTitle by remember { mutableStateOf(options.get(0).title) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var oldSelectedIndex by remember { mutableStateOf(0) }
    var canBeSelected by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState { options.size }
    val scope = rememberCoroutineScope()

    val viewModel = getViewModelByOptionType(optionType = optionType)

    // share reader connection status to other viewmodels
    viewModel.updateIsConnectedStatus(isConnected)

    val screens = getScreensByOptionType(optionType = optionType, viewModel = viewModel)
    var isSaved by remember { mutableStateOf(false) }
    val detailUiState by viewModel.detailUiState.collectAsState()

    val showAuthorizationFailedDialog by viewModel.showAuthorizationFailedDialog.collectAsState()
    //TODO delete it later
//    val showAuthorizationFailedDialog by remember {
//        mutableStateOf(true)
//    }
    var showErrorDialog by remember { mutableStateOf(false) }

    ReaderLifeCycle(viewModel = viewModel)

    if (showAuthorizationFailedDialog) AuthorizationTokenExpiredDialog(
        message = AUTHORIZATION_FAILED_MESSAGE,
        onLogOut = logOut
    )

    LaunchedEffect(detailUiState) {
        isSaved = detailUiState.isSaved
        showErrorDialog = detailUiState.message.isNotEmpty()
    }

    // show loading while data is fetching
    LoadingDialog(
        title = "Loading...",
        showDialog = detailUiState.showLoadingDialog,
        onDismiss = { }
    )

    // show error when error message is not empty
    WarningDialog(
        icon = CustomIcon.Vector(Icons.Default.Error),
        message = detailUiState.message,
        showDialog = showErrorDialog,
        positiveButtonTitle = "Ok",
        onDismiss = {
            showErrorDialog = false
        }
    )

    LaunchedEffect(pagerState.currentPage) {
        val option = options.get(pagerState.currentPage)
        tabTitle = option.title
        if (pagerState.currentPage == options.size - 1) showConfirmationDialog = true
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

            ConfirmationDialog(
                showDialog = showConfirmationDialog,
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
                }
            )

            Column {
                TabBarLayout(
                    options = screens,
                    selected = canBeSelected,
                    pagerState = pagerState,
                    oldSelectedIndex = oldSelectedIndex,
                    onTabSelected = { title, index ->
                        tabTitle = title

                        if (canBeSelected) oldSelectedIndex = index
                        // check whether current tab item is exit tab
                        if (title.equals(options.get(options.size - 1).title)) {
                            showConfirmationDialog = true
                        }
                    }
                )

                HorizontalPager(state = pagerState, userScrollEnabled = false) {
                    /** Check current screen is scan screen or not
                     * If not, make scanner not readable */
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