@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.etag.ReaderLifeCycle
import com.etag.stsyn.R
import com.etag.stsyn.ui.components.LoginProgressDialog
import com.etag.stsyn.ui.components.VersionText
import com.etag.stsyn.ui.theme.Purple80
import com.tzh.retrofit_module.util.ApiResponse

@Composable
fun LoginScreen(
    navigateToLoginContentScreen: () -> Unit,
    modifier: Modifier = Modifier.fillMaxSize(),
    loginViewModel: LoginViewModel
) {

    val loginUiState by loginViewModel.loginUiState.collectAsState()
    val rfidUiState by loginViewModel.rfidUiState.collectAsState()
    val context = LocalContext.current

    ReaderLifeCycle(viewModel = loginViewModel)
    LaunchedEffect(Unit) {
        Log.d("TAG", "LoginScreen: Unit - ${rfidUiState.isConnected}")
    }
    var showLoadingDialog by remember {
        mutableStateOf(false)
    }
    var showInvalidUserDialog by remember {
        mutableStateOf(false)
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        loginViewModel.getUserByEPCResponse.collect { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    showLoadingDialog = true
                }

                is ApiResponse.Success -> {
                    showLoadingDialog = false
                    val userModel = response.data?.userModel


                    if (userModel != null) {
                        navigateToLoginContentScreen()
                        loginViewModel.saveUserByEpcResponseToLocal(userModel)
                    } else {
                        showInvalidUserDialog = true
                        Toast.makeText(context, "Invalid user Id", Toast.LENGTH_LONG).show()
                    }
                }

                is ApiResponse.ApiError -> {
                    showLoadingDialog = false
                }

                else -> {
                    showLoadingDialog = false
                }
            }
        }
    }
    /*
  *
  *  LaunchedEffect(loginUiState.rfidId) {
        if (loginUiState.rfidId.isNotEmpty()) {
            navigateToLoginContentScreen()
        }
    }
  **/






    LoginProgressDialog(showDialog = showLoadingDialog)



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
        MainLowerContent(onScan = { loginViewModel.toggle() })
    }
}

@Composable
private fun MainLowerContent(
    onScan: () -> Unit,
    modifier: Modifier = Modifier
) {
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
