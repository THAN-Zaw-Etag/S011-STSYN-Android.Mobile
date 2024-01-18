@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen.main

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.etag.ReaderLifeCycle
import com.etag.stsyn.R
import com.etag.stsyn.ui.components.AppBar
import com.etag.stsyn.ui.components.BottomNavigationBar
import com.etag.stsyn.ui.components.ChangePasswordDialog
import com.etag.stsyn.ui.components.CustomIcon
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.ProfileTextButton
import com.etag.stsyn.ui.components.VersionText
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.ui.navigation.HomeNavigationGraph
import com.etag.stsyn.ui.screen.login.LoginViewModel
import com.etag.stsyn.ui.viewmodel.SharedUiViewModel
import com.etag.stsyn.util.TransitionUtil
import com.tzh.retrofit_module.data.model.LocalUser
import com.tzh.retrofit_module.util.ApiResponse
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onChangePassword: (String, String) -> Unit,
    onLogOutClick: () -> Unit,
    onSettingsClick: () -> Unit,
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
    val savedUserState by loginViewModel.savedUser.collectAsState(initial = LocalUser())

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val sharedUiViewModel: SharedUiViewModel = hiltViewModel()
    val navController = rememberNavController()
    val sharedUiState by sharedUiViewModel.uiState.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    val updatePasswordResponse by loginViewModel.updatePasswordResponse.collectAsState()
    val context = LocalContext.current

    ReaderLifeCycle(viewModel = loginViewModel)

    when (updatePasswordResponse) {
        is ApiResponse.Loading -> LoadingDialog(
            title = "Updating password...",
            showDialog = true,
            onDismiss = { /*TODO*/ }
        )

        is ApiResponse.Success -> {
            Toast.makeText(context, "Password updated successfully!", Toast.LENGTH_SHORT).show()
        }

        is ApiResponse.ApiError -> {
            showErrorDialog = true
        }

        else -> {}
    }

    WarningDialog(
        icon = CustomIcon.Vector(Icons.Default.Error),
        message = if (showErrorDialog) (updatePasswordResponse as ApiResponse.ApiError).message else "",
        showDialog = showErrorDialog,
        positiveButtonTitle = "try again",
        onPositiveButtonClick = {
            showErrorDialog = false
        }
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = modifier,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(
                    user = savedUserState,
                    onChangePassword = onChangePassword,
                    onSettingsClick = onSettingsClick,
                    onLogOutClick = onLogOutClick,
                )
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    selectedItem = sharedUiState.selectedBottomNavigationItem,
                    showBottomBar = sharedUiState.showBottomNavigationBar,
                    onBottomNavigationItemSelected = { navController.navigate(it) })
            },
            topBar = {
                AnimatedVisibility(
                    visible = sharedUiState.showTopAppBar,
                    enter = TransitionUtil.slideInVerticallyFromTop,
                    exit = TransitionUtil.slideOutVerticallyToTop
                ) {
                    AppBar(
                        userName = savedUserState.name,
                        title = sharedUiState.title,
                        onDrawerIconClick = {
                            coroutineScope.launch { drawerState.open() }
                        }
                    )
                }
            },
        ) {
            HomeNavigationGraph(
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
    onChangePassword: (String, String) -> Unit,
    onSettingsClick: () -> Unit,
    onLogOutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) ChangePasswordDialog(
        userName = user.name,
        onChangePassword = onChangePassword,
        showDialog = showDialog,
        onDismiss = {
            showDialog = false
        }
    )

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
                modifier = Modifier
                    .align(Alignment.Center)
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
                            text = user.nric,
                            color = Color.White
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        ProfileTextButton(
            text = "Change Password",
            onOptionClick = {
                showDialog = true
            },
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        )
        ProfileTextButton(
            text = "Settings",
            onOptionClick = onSettingsClick,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        )
        ProfileTextButton(
            text = "Logout",
            onOptionClick = onLogOutClick,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))
        VersionText(modifier = Modifier.padding(16.dp))
    }
}