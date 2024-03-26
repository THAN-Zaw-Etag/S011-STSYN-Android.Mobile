@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.etag.stsyn.util.PasswordValidator
import com.etag.stsyn.util.doesNotEquals

const val TAG = "ChangePasswordDialog"

@Composable
fun UpdatePasswordDialog(
    userName: String,
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onChangePassword: (String, String) -> Unit
) {
    // State variables
    var show by remember { mutableStateOf(false) }
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordMismatch by remember { mutableStateOf(false) }

    // Effects
    LaunchedEffect(showDialog) { show = showDialog }

    LaunchedEffect(newPassword, confirmPassword) {
        isPasswordMismatch = newPassword.trim() != confirmPassword.trim()
    }

    // Dialog content
    if (show) {
        Dialog(onDismissRequest = onDismiss) {
            DialogContent(
                modifier = modifier,
                userName = userName,
                onDismiss = {
                    show = false
                    onDismiss()
                },
                onPasswordChange = {
                    onChangePassword(oldPassword, newPassword)
                },
                isPasswordMismatch = isPasswordMismatch,
                onOldPasswordChange = { oldPassword = it },
                onNewPasswordChange = { newPassword = it },
                onConfirmPasswordChange = { confirmPassword = it },
                errorMessage = getErrorMessage(newPassword, confirmPassword)
            )
        }
    }
}

@Composable
private fun DialogContent(
    modifier: Modifier,
    userName: String,
    onDismiss: () -> Unit,
    onPasswordChange: () -> Unit,
    isPasswordMismatch: Boolean,
    onOldPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    errorMessage: String
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 32.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
    ) {
        IconButton(
            onClick = onDismiss,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Username
            Row(verticalAlignment = Alignment.CenterVertically) {
                UserNameShortcutIcon(name = userName, modifier = Modifier.padding(0.dp))
                Spacer(modifier = Modifier.width(22.dp))
                Text(text = userName)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Password fields
            ChangePasswordField(
                hint = "Old Password",
                isError = false,
                onValueChange = onOldPasswordChange
            )
            ChangePasswordField(
                hint = "New Password",
                isError = isPasswordMismatch || errorMessage.isNotEmpty(),
                onValueChange = onNewPasswordChange
            )
            ChangePasswordField(
                hint = "Confirm Password",
                isError = isPasswordMismatch || errorMessage.isNotEmpty(),
                onValueChange = onConfirmPasswordChange,
                showVisibilityIcon = false
            )

            // Error message
            if (errorMessage.isNotEmpty()) {
                ErrorText(
                    text = errorMessage,
                    modifier = Modifier.padding(top = 8.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Save button
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                onClick = onPasswordChange
            ) {
                Text(text = "Save")
            }

            Spacer(modifier = Modifier.weight(1f))

            // Password requirements
            ErrorText(
                text = "*Password Requirements: Minimum 12 characters (at least one upper case, lower case, special character, and number)",
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun getErrorMessage(newPassword: String, confirmPassword: String): String {
    // Validate passwords and return error message if any
    return when {
        newPassword.isEmpty() || confirmPassword.isEmpty() -> "Password fields must not be empty"
        newPassword != confirmPassword -> "Passwords do not match"
        else -> ""
    }
}

@Composable
private fun ChangePasswordField(
    hint: String,
    isError: Boolean,
    showVisibilityIcon: Boolean = true,
    onValueChange: (String) -> Unit,
) {
    PasswordField(
        isError = isError,
        onValueChange = onValueChange,
        cornerRadius = 10.dp,
        showVisibilityIcon = showVisibilityIcon,
        borderColor = Color.LightGray,
        hint = hint
    )
}
