package com.etag.stsyn.ui.screen.login

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.etag.stsyn.R
import com.etag.stsyn.data.model.LocalUser
import com.etag.stsyn.ui.components.CustomIcon
import com.etag.stsyn.ui.components.ExitApp
import com.etag.stsyn.ui.components.LoadingDialog
import com.etag.stsyn.ui.components.PasswordField
import com.etag.stsyn.ui.components.VersionText
import com.etag.stsyn.ui.components.WarningDialog
import com.etag.stsyn.ui.theme.Purple80
import com.etag.stsyn.util.MAXIMUM_LOGIN_ATTEMPTS
import com.etag.stsyn.util.PasswordValidator
import com.etag.stsyn.util.toLines
import com.tzh.retrofit_module.domain.model.login.LoginResponse
import com.tzh.retrofit_module.util.ApiResponse

@Composable
fun LoginContentScreen(
    loginAttemptCount: Int,
    isSuccessful: Boolean,
    userName: String,
    loginResponse: ApiResponse<LoginResponse>,
    modifier: Modifier = Modifier,
    onLogInClick: (String) -> Unit,
    onSuccess: (LocalUser) -> Unit,
    onFailed: () -> Unit,
) {
    var error by remember { mutableStateOf("") }
    var showWarningDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(loginAttemptCount) {
        showWarningDialog = loginAttemptCount == MAXIMUM_LOGIN_ATTEMPTS
    }

    LaunchedEffect(error) {
        Log.d("TAG", "LoginResponse: $error")
    }


    when (loginResponse) {
        is ApiResponse.Loading -> LoadingDialog(title = "Signing In...",
            showDialog = true,
            onDismiss = { })

        is ApiResponse.Success -> {
            println("response: success")
            val user = loginResponse.data?.user
            val localUser = LocalUser(
                name = user?.userName ?: "",
                id = user?.userId ?: "",
                nric = user?.nric ?: "",
                token = loginResponse.data?.token ?: ""
            )
            onSuccess(localUser)
        }

        is ApiResponse.Error -> {
            error = loginResponse.message
        }

        else -> {}
    }

    WarningDialog(icon = CustomIcon.Vector(Icons.Default.Warning),
        message = "You've tried to log in 10 times.",
        showDialog = showWarningDialog,
        positiveButtonTitle = "exit",
        onPositiveButtonClick = { ExitApp(context) })

    LaunchedEffect(isSuccessful) {
        if (isSuccessful) Toast.makeText(
            context, "Login successful!", Toast.LENGTH_SHORT
        ).show()
    }

    Column(modifier = modifier.fillMaxSize()) {
        LoginUpperSection()
        Spacer(modifier = Modifier.height(16.dp))
        LoginSection(onLogInClick = onLogInClick, errorMessage = error, userName = userName)
        Spacer(modifier = Modifier.weight(1f))
        LoginLowerSection()
    }
}

@Composable
private fun LoginLowerSection(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.login_lower),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )

        VersionText(
            color = Color.White, modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        )
    }
}

@Composable
private fun LoginSection(
    errorMessage: String,
    userName: String,
    onLogInClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val errorMessages = remember { mutableStateListOf<String>() }
    var showError by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf(errorMessage) }

    LaunchedEffect(errorMessages.size) {
        showError = errorMessages.isNotEmpty()
        error = errorMessages.toLines()
    }

    LaunchedEffect(errorMessage) {
        showError = errorMessage.isNotEmpty()
        error = errorMessage
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {
        var enteredPassword by remember { mutableStateOf("") }
        Text(
            text = "Welcome, $userName",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Purple80
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Please enter your password", fontWeight = FontWeight.Bold, color = Purple80
        )
        Spacer(modifier = Modifier.height(24.dp))
        PasswordField(hint = "Password", isError = showError, onValueChange = {
            enteredPassword = it
            errorMessages.clear()
        }, onSubmit = {
            errorMessages.clear() // clear old error messages
            val messages = PasswordValidator.validatePassword(enteredPassword)
            errorMessages.addAll(messages)
            if (errorMessages.isEmpty()) onLogInClick(it) // if there is no error, allow to login
        })
        Spacer(modifier = Modifier.height(8.dp))
        AnimatedVisibility(visible = showError) {
            Text(text = error, color = Color.Red)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {
            Button(
                onClick = {
                    errorMessages.clear() // clear old error messages
                    errorMessages.addAll(PasswordValidator.validatePassword(enteredPassword))
                    if (errorMessages.isEmpty()) onLogInClick(enteredPassword)
                }, colors = ButtonDefaults.buttonColors(containerColor = Purple80)
            ) {
                Text(text = "Log in", modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

@Composable
private fun LoginUpperSection(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.login_upper),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.scale(1.5f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Store Management System", color = Color.White
            )
        }
    }
}