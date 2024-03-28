@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen.login

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.etag.ReaderLifeCycle
import com.etag.stsyn.R
import com.etag.stsyn.enums.DeviceSize
import com.etag.stsyn.ui.components.CustomIcon
import com.etag.stsyn.ui.components.ErrorDialog
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.ShowBaseUrlAlertDialog
import com.etag.stsyn.ui.components.VersionText
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.ui.states.rememberMutableDialogState
import com.etag.stsyn.ui.theme.Purple80
import com.etag.stsyn.util.AppUtil.getDeviceSize
import com.tzh.retrofit_module.domain.model.user.GetUserByEPCResponse
import com.tzh.retrofit_module.util.ApiResponse

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigateToLoginContentScreen: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel(),
) {
    ReaderLifeCycle(viewModel = loginViewModel)

    val rfidUiState by loginViewModel.rfidUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var showLoadingDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var showInvalidUserDialog by remember { mutableStateOf(false) }
    val validationErrorMessage by loginViewModel.validationErrorMessage.collectAsStateWithLifecycle()
    val getUserByEPCResponse by loginViewModel.getUserByEPCResponse.collectAsStateWithLifecycle()
    val dialogState = rememberMutableDialogState(data = "")

    val shouldShowEmptyBaseUrlDialog by loginViewModel.shouldShowEmptyBaseUrlDialog.collectAsStateWithLifecycle(true)

    ShowBaseUrlAlertDialog(
        showAlertDialog = shouldShowEmptyBaseUrlDialog || validationErrorMessage.isNotEmpty(),
        validationErrorMessage = validationErrorMessage,
        onConfirm = loginViewModel::validateBaseUrl
    )

    LaunchedEffect(getUserByEPCResponse) {
        when (getUserByEPCResponse) {
            is ApiResponse.Loading -> {
                showLoadingDialog = true
                showErrorDialog = false
                dialogState.hideDialog()
            }

            is ApiResponse.Success -> {
                showLoadingDialog = false
                dialogState.hideDialog()

                val userModel = (getUserByEPCResponse as ApiResponse.Success<GetUserByEPCResponse>).data?.userModel

                if (userModel != null) {
                    navigateToLoginContentScreen()
                } else {
                    showInvalidUserDialog = true
                    Toast.makeText(context, "Invalid user Id", Toast.LENGTH_LONG).show()
                }
            }

            is ApiResponse.ApiError -> {
                val message = (getUserByEPCResponse as ApiResponse.ApiError).message
                dialogState.showDialog(message)
                showLoadingDialog = false
                errorMessage = message
            }

            else -> {
                dialogState.hideDialog()
                showLoadingDialog = false
                showErrorDialog = false
            }
        }
    }

    WarningDialog(icon = CustomIcon.Vector(Icons.Default.Warning), dialogState = dialogState, positiveButtonTitle = "Ok")

    ErrorDialog(
        showDialog = showErrorDialog,
        errorTitle = "Error",
        errorMessage = errorMessage,
        onDismiss = { showErrorDialog = false }
    )

    LoadingDialog(
        title = "ID verifying...",
        showDialog = showLoadingDialog,
        onDismiss = { }
    )

    LaunchedEffect(rfidUiState.isConnected) {
        if (rfidUiState.isConnected) Toast.makeText(context, "Connected!", Toast.LENGTH_SHORT).show()
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
        MainLowerContent(onScan = loginViewModel::getUserByRfidId)
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
        DeviceSize.MEDIUM -> {}
        DeviceSize.TABLET -> {}
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
