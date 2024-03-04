@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.etag.ReaderLifeCycle
import com.etag.stsyn.R
import com.etag.stsyn.enums.DeviceSize
import com.etag.stsyn.ui.components.ErrorDialog
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.ShowBaseUrlAlertDialog
import com.etag.stsyn.ui.components.VersionText
import com.etag.stsyn.ui.theme.Purple80
import com.etag.stsyn.util.AppUtil.getDeviceSize
import com.tzh.retrofit_module.data.settings.AppConfigModel
import com.tzh.retrofit_module.util.ApiResponse

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigateToLoginContentScreen: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel(),
) {
    val rfidUiState by loginViewModel.rfidUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ReaderLifeCycle(viewModel = loginViewModel)

    var showLoadingDialog by remember {
        mutableStateOf(false)
    }
    var showErrorDialog by remember {
        mutableStateOf(false)
    }
    var errorMessage by remember {
        mutableStateOf("")
    }

    var showInvalidUserDialog by remember {
        mutableStateOf(false)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val appConfiguration by loginViewModel.appConfig.collectAsStateWithLifecycle(initialValue = AppConfigModel())
    val emptyBaseUrlStatus = loginViewModel.shouldShowEmptyBaseUrlDialog.observeAsState(false)
    if (emptyBaseUrlStatus.value) {
        ShowBaseUrlAlertDialog(
            onConfirm = {
                loginViewModel.updateAppConfig(appConfiguration.copy(apiUrl = "https://18.139.63.32/SMS-STSYN-Dev/api/"))   //TODO change this when app release
            }) {

        }
    }

    LaunchedEffect(lifecycleOwner) {
        loginViewModel.getUserByEPCResponse.collect { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    showLoadingDialog = true
                    showErrorDialog = false
                    Log.d("TAGUser", "LoginScreen: Loading")
                }

                is ApiResponse.Success -> {
                    Log.d("TAGUser", "LoginScreen: Success")
                    showLoadingDialog = false
                    showErrorDialog = false
                    val userModel = response.data?.userModel

                    if (userModel != null) {
                        Log.d("TAG", "LoginScreen: validId")
                        navigateToLoginContentScreen()
                        loginViewModel.saveUserByEpcResponseToLocal(userModel)
                    } else {
                        showInvalidUserDialog = true
                        Toast.makeText(context, "Invalid user Id", Toast.LENGTH_LONG).show()
                    }
                }

                is ApiResponse.ApiError -> {
                    val error = response.message
                    showErrorDialog = true
                    errorMessage = error
                    Log.d("TAGUser", "LoginScreen: ApiError")
                    showLoadingDialog = false
                }

                else -> {
                    Log.d("TAGUser", "LoginScreen: else")
                    showLoadingDialog = false
                    showErrorDialog = false
                }
            }
        }
    }
    Surface {
        ErrorDialog(
            showDialog = showErrorDialog,
            errorTitle = "Error",
            errorMessage = errorMessage,
            onDismiss = { showErrorDialog = false }
        )
    }

    LoadingDialog(title = "ID verifying...",
        showDialog = showLoadingDialog,
        onDismiss = { }
    )

    LaunchedEffect(rfidUiState.isConnected) {
        if (rfidUiState.isConnected) Toast.makeText(context, "Connected!", Toast.LENGTH_SHORT)
            .show()
    }

    Column(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.main_upper),
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth,
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Store Management System",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Purple80,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        MainLowerContent(onScan = {
            //TODO delete this loginViewModel.getUserByRfidId("455341303030303030303130") when app release
            loginViewModel.getUserByRfidId("455341303030303030303130")
        })
    }
}

@Composable
private fun MainLowerContent(
    onScan: () -> Unit,
    modifier: Modifier = Modifier
) {
    var quickLoginTextFontSize = 16.sp
    when (getDeviceSize()) {
        DeviceSize.SMALL -> quickLoginTextFontSize = MaterialTheme.typography.titleSmall.fontSize
        DeviceSize.MEDIUM -> Log.d("TAG", "LoginScreen: MEDIUM")
        DeviceSize.TABLET -> Log.d("TAG", "LoginScreen: TABLET")
    }
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.main_lower),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .rotate(180f)
                .wrapContentHeight()
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Now! Quick Login use RFID",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontSize = quickLoginTextFontSize,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.rfid_icon),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .clickable { onScan() },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Use RFID", fontWeight = FontWeight.Medium, color = Color.White)
        }

        VersionText(
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        )
    }
}
