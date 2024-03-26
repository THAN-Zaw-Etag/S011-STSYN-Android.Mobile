@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen.main

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.etag.ReaderLifeCycle
import com.etag.stsyn.R
import com.etag.stsyn.ui.components.AnimatedAppBar
import com.etag.stsyn.ui.components.AuthorizationTokenExpiredDialog
import com.etag.stsyn.ui.components.BottomNavigationBar
import com.etag.stsyn.ui.components.UpdatePasswordDialog
import com.etag.stsyn.ui.components.ConfirmationDialog
import com.etag.stsyn.ui.components.CustomIcon
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.ProfileTextButton
import com.etag.stsyn.ui.components.VersionText
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.ui.navigation.HomeNavigationGraph
import com.etag.stsyn.ui.navigation.Routes
import com.etag.stsyn.ui.screen.login.LoginViewModel
import com.etag.stsyn.ui.states.mutableDialogStateOf
import com.etag.stsyn.ui.viewmodel.SharedUiViewModel
import com.tzh.retrofit_module.util.log.Logger
import com.tzh.retrofit_module.data.model.LocalUser
import com.tzh.retrofit_module.util.AUTHORIZATION_FAILED_MESSAGE
import com.tzh.retrofit_module.util.ApiResponse
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction2

@Composable
fun HomeScreen(
    isSodInitiate: Boolean,
    onChangePassword: KFunction2<CharArray, CharArray, Unit>,
    onLogOutClick: () -> Unit,
    sharedUiViewModel: SharedUiViewModel,
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
    val savedUserState by loginViewModel.savedUser.collectAsStateWithLifecycle()
    val showAuthorizationFailedDialog by loginViewModel.showAuthorizationFailedDialog.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()
    val sharedUiState by sharedUiViewModel.uiState.collectAsStateWithLifecycle()
    val dialogState = remember { mutableDialogStateOf("") }
    val updatePasswordResponse by loginViewModel.updatePasswordResponse.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showLoadingDialog by remember { mutableStateOf(false) }
    var showUpdatePasswordDialog by remember { mutableStateOf(false) }

    ReaderLifeCycle(viewModel = loginViewModel)

    LaunchedEffect(savedUserState) {
        Logger.d("HomeScreen: $savedUserState")
    }

    LaunchedEffect(updatePasswordResponse) {
        when (updatePasswordResponse) {
            is ApiResponse.Loading -> showLoadingDialog = true

            is ApiResponse.Success -> {
                showLoadingDialog = false
                Toast.makeText(context, "Password updated successfully!", Toast.LENGTH_SHORT).show()
            }

            is ApiResponse.ApiError -> {
                showLoadingDialog = false
                dialogState.showDialog((updatePasswordResponse as ApiResponse.ApiError).message)
            }

            else -> {
                showLoadingDialog = false
                dialogState.hideDialog()
            }
        }
    }

    LoadingDialog(
        title = "Updating password...",
        showDialog = showLoadingDialog,
        onDismiss = { })

    if (showAuthorizationFailedDialog) AuthorizationTokenExpiredDialog(
        message = AUTHORIZATION_FAILED_MESSAGE, onLogOut = onLogOutClick
    )

    UpdatePasswordDialog(
        userName = savedUserState.name,
        onChangePassword = { old, new ->
            onChangePassword(old.toCharArray(), new.toCharArray())
        },
        showDialog = showUpdatePasswordDialog,
        onDismiss = {
            showUpdatePasswordDialog = false
        }
    )

    WarningDialog(
        icon = CustomIcon.Vector(Icons.Default.Error),
        dialogState = dialogState,
        positiveButtonTitle = "ok",
        onPositiveButtonClick = dialogState::hideDialog
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = modifier,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(
                    user = savedUserState,
                    onChangePasswordClick = {
                        coroutineScope.launch {
                            drawerState.close()
                            showUpdatePasswordDialog = true
                        }
                    },
                    onSettingsClick = {
                        coroutineScope.launch { drawerState.close() }
                        navController.navigate(Routes.SettingsScreen.name)
                    },
                    onLogOutClick = onLogOutClick,
                )
            }
        }) {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    selectedItem = sharedUiState.selectedBottomNavigationItem,
                    showBottomBar = sharedUiState.showBottomNavigationBar,
                    onBottomNavigationItemSelected = { navController.navigate(it) }
                )
            },
            topBar = {
                AnimatedAppBar(
                    visible = sharedUiState.showTopAppBar,
                    userName = savedUserState.name,
                    title = sharedUiState.title,
                    icon = sharedUiState.icon,
                    onIconClick = {
                        if (sharedUiState.icon == Icons.AutoMirrored.Filled.ArrowBack) {
                            navController.navigateUp()
                        } else coroutineScope.launch {
                            drawerState.open()
                        }
                    }
                )
            },
        ) {
            HomeNavigationGraph(
                isSodInitiate = isSodInitiate,
                loginViewModel = loginViewModel,
                sharedUiViewModel = sharedUiViewModel,
                navController = navController,
                modifier = Modifier.padding(it)
            )
        }
    }
}


@Composable
private fun DrawerContent(
    user: LocalUser,
    onChangePasswordClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogOutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLogOutDialog by remember { mutableStateOf(false) }

    ConfirmationDialog(showDialog = showLogOutDialog,
        title = "Log Out?",
        cancelTitle = "Cancel",
        confirmTitle = "Ok",
        onCancelClick = { },
        onDismiss = { showLogOutDialog = false },
        onConfirmClick = { onLogOutClick() })

    Column(
        modifier = modifier
            .fillMaxWidth(0.8f)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(modifier = modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.drawer_content_upper),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    tint = Color.White,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.account_id_icon),
                            contentDescription = null,
                            modifier = Modifier.scale(1.5f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = user.nric, color = Color.White
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        ProfileTextButton(
            text = "Change Password",
            onOptionClick = onChangePasswordClick,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        )
        if (user.isAdmin) ProfileTextButton(
            text = "Settings",
            onOptionClick = onSettingsClick,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        )
        ProfileTextButton(
            text = "Logout",
            onOptionClick = { showLogOutDialog = true },
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))
        VersionText(modifier = Modifier.padding(16.dp))
    }
}