package com.etag.stsyn.ui.screen.main

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.etag.stsyn.R
import com.etag.stsyn.ui.components.PasswordField
import com.etag.stsyn.ui.components.VersionText
import com.etag.stsyn.ui.theme.Purple80

@Composable
fun LoginContentScreen(
    isSuccessful: Boolean,
    errorMessage: String,
    onLogInClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var error by remember { mutableStateOf("") }

    LaunchedEffect(errorMessage) {
        error = errorMessage
    }

    val context = LocalContext.current
    LaunchedEffect(isSuccessful) {
        Toast.makeText(
            context,
            if (isSuccessful) "Login successful!" else "Login failed!",
            Toast.LENGTH_SHORT
        ).show()
    }

    Column(modifier = modifier.fillMaxSize()) {
        LoginUpperSection()
        Spacer(modifier = Modifier.height(16.dp))
        LoginSection(onLogInClick = onLogInClick, error)
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
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        )

    }
}

@Composable
private fun LoginSection(
    onLogInClick: (String) -> Unit,
    errorMessage: String,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {
        var enteredPassword by remember { mutableStateOf("") }
        Text(
            text = "Welcome, Admin",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Purple80
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Please enter your password",
            fontWeight = FontWeight.Bold,
            color = Purple80
        )
        Spacer(modifier = Modifier.height(24.dp))
        PasswordField(
            hint = "Password",
            onValueChange = { enteredPassword = it },
            onSubmit = { onLogInClick(it) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        AnimatedVisibility(visible = errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {
            Button(
                onClick = { onLogInClick(enteredPassword) },
                colors = ButtonDefaults.buttonColors(containerColor = Purple80)
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
                text = "Store Management System",
                color = Color.White
            )
        }
    }
}