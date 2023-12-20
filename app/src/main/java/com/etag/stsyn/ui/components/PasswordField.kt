package com.etag.stsyn.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun PasswordField(
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier
) {
    var showPassword by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        label = { Text(text = hint) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(40.dp),
        onValueChange = {
            text = it
            onValueChange(it)
        },
        visualTransformation = if (showPassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }, trailingIcon = {
            if (showPassword) {
                IconButton(onClick = { showPassword = false }) {
                    Icon(imageVector = Icons.Filled.Visibility, contentDescription = null)
                }
            } else {
                IconButton(onClick = { showPassword = true }) {
                    Icon(imageVector = Icons.Filled.VisibilityOff, contentDescription = null)
                }
            }
        }
    )
}