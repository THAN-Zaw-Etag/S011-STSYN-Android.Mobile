@file:OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class
)

package com.etag.stsyn.ui.screen.detail

import android.util.Log
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.etag.ReaderLifeCycle
import com.etag.stsyn.core.ClickEvent
import com.etag.stsyn.enums.OptionType
import com.etag.stsyn.ui.components.AuthorizationTokenExpiredDialog
import com.etag.stsyn.ui.components.ConfirmationDialog
import com.etag.stsyn.ui.components.CustomIcon
import com.etag.stsyn.ui.components.DisableBackPress
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.SuccessDialog
import com.etag.stsyn.ui.components.TabBarLayout
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.ui.screen.acct_check.AccountCheckViewModel
import com.etag.stsyn.ui.screen.login.Shift
import com.etag.stsyn.ui.states.rememberMutableDialogState
import com.etag.stsyn.util.TabUtil
import com.etag.stsyn.util.TransitionUtil
import com.etag.stsyn.util.datasource.getScreensByOptionType
import com.etag.stsyn.util.datasource.getViewModelByOptionType
import com.tzh.retrofit_module.util.AUTHORIZATION_FAILED_MESSAGE
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "DetailScreen"

@Composable
fun DetailScreen(
    isConnected: Boolean,
    optionType: OptionType,
    shiftType: Shift,
    modifier: Modifier = Modifier,
    logOut: () -> Unit,
    navigateToMainMenu: () -> Unit,
    navigateToHomeScreen: () -> Unit,
) {

    var showTabBar by remember { mutableStateOf(false) }
    val options = TabUtil.getTabDetails(optionType)
    var tabTitle by remember { mutableStateOf(options[0].title) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState { options.size }
    val scope = rememberCoroutineScope()

    val viewModel = getViewModelByOptionType(optionType = optionType)

    val screens = getScreensByOptionType(optionType = optionType, viewModel = viewModel)
    var isSaved by remember { mutableStateOf(false) }
    val detailUiState by viewModel.detailUiState.collectAsStateWithLifecycle()

    val showAuthorizationFailedDialog by viewModel.showAuthorizationFailedDialog.collectAsStateWithLifecycle()
    val clickEventFlow by viewModel.clickEventFlow.collectAsStateWithLifecycle(ClickEvent.Default)

    val errorDialogState = rememberMutableDialogState(data = "")
    val successDialogState = rememberMutableDialogState(data = "")
    var previousIndex by remember { mutableIntStateOf(0) }

    ReaderLifeCycle(viewModel = viewModel)
    DisableBackPress()

    LaunchedEffect(Unit) {
        // share reader connection status to other viewmodel
        viewModel.updateIsConnectedStatus(isConnected)
        if (viewModel is AccountCheckViewModel) viewModel.setShiftType(shiftType)
    }

    if (showAuthorizationFailedDialog) AuthorizationTokenExpiredDialog(
        message = AUTHORIZATION_FAILED_MESSAGE, onLogOut = logOut
    )

    LaunchedEffect(detailUiState) {
        isSaved = detailUiState.isSaved
        if (detailUiState.showSuccessDialog) successDialogState.showDialog("SUCCESS!")
        errorDialogState.showDialog(detailUiState.message)
    }

    LaunchedEffect(clickEventFlow) {
        when (clickEventFlow) {
            is ClickEvent.ClickAfterSave -> {
                scope.launch { pagerState.animateScrollToPage(0) }
            }
            is ClickEvent.ClickToNavigateHome -> navigateToMainMenu()
            else -> { return@LaunchedEffect }
        }
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
        dialogState = errorDialogState,
        positiveButtonTitle = "Try again",
        negativeButtonTitle = "Cancel",
        onPositiveButtonClick = {
            viewModel.updateClickEvent(ClickEvent.RetryClick)
        }
    )

    // show success dialog when saving items is done
    SuccessDialog(
        state = successDialogState,
        onDoneClick = {
            viewModel.updateClickEvent(ClickEvent.ClickAfterSave)
        }
    )

    LaunchedEffect(pagerState.currentPage) {
        val option = options[pagerState.currentPage]
        tabTitle = option.title
        if (pagerState.currentPage == options.size - 1) showConfirmationDialog = true
    }

    LaunchedEffect(Unit) {
        delay(300)
        showTabBar = true
    }


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
                    scope.launch { pagerState.animateScrollToPage(previousIndex) }
                },
                onConfirmClick = {
                    showConfirmationDialog = false
                    navigateToHomeScreen()
                }
            )

            Column {
                TabBarLayout(
                    options = screens,
                    pagerState = pagerState,
                    onTabSelected = { title, index ->
                        tabTitle = title

                        // click index is not exit button, set selected index as previous index
                        if (index != screens.size - 1) previousIndex = index
                        // check whether current tab item is exit tab
                        if (title == options[options.size - 1].title) {
                            showConfirmationDialog = true
                        }
                    })

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
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            Text("No screen available")
                        }
                    }
                }
            }
        }
    }
}