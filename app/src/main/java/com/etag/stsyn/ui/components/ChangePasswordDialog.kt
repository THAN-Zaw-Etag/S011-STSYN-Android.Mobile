@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.components

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.etag.stsyn.util.PasswordValidator
import com.etag.stsyn.util.doesNotEquals

const val TAG = "ChangePasswordDialog"

@Composable
fun ChangePasswordDialog(
    userName: String,
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onChangePassword: (String, String) -> Unit
) {
    var show by remember { mutableStateOf(false) }
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isWrongPassword by remember { mutableStateOf(false) }

    LaunchedEffect(showDialog) {
        show = showDialog
    }

    LaunchedEffect(newPassword, confirmPassword) {
        isWrongPassword = newPassword.trim().doesNotEquals(confirmPassword.trim())
    }

    if (show) {
        Dialog(onDismissRequest = onDismiss) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(vertical = 32.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
            ) {
                IconButton(
                    onClick = {
                        show = false
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
                // password fields
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        UserNameShortcutIcon(name = userName, modifier = Modifier.padding(0.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = userName)
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    ChangePasswordField(
                        hint = "Old Password",
                        isError = false,
                        onValueChange = { oldPassword = it }
                    )
                    ChangePasswordField(
                        hint = "New Password",
                        isError = isWrongPassword || if (newPassword.isNotEmpty()) PasswordValidator.validatePassword(
                            newPassword
                        ).isNotEmpty() else false,
                        onValueChange = { newPassword = it }
                    )
                    ChangePasswordField(
                        hint = "Confirm Password",
                        isError = isWrongPassword || if (confirmPassword.isNotEmpty()) PasswordValidator.validatePassword(
                            confirmPassword
                        ).isNotEmpty() else false,
                        onValueChange = { confirmPassword = it },
                        showVisibilityIcon = false
                    )
                    FilledTonalButton(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = {
                            onChangePassword(oldPassword, newPassword)
                            onDismiss()
                        }
                    ) {
                        Text(text = "Save")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    // warning text
                    ErrorText(
                        text = "*Password Requirements: Minimum 12 characters (at least one upper case, lower case and special characters, and number)",
                        modifier = Modifier
                            .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                    )
                }
            }
        }
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
