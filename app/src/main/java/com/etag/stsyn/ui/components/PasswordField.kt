@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.theme.errorColor

/**
 * PasswordField
 * @param onValueChange Callback function to return input value
 * @param hint Value to show as hint*/

@Composable
fun PasswordField(
    isError: Boolean,
    onValueChange: (String) -> Unit,
    hint: String,
    cornerRadius: Dp = 40.dp,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    showVisibilityIcon: Boolean = true,
    onSubmit: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {

    var showError by remember { mutableStateOf(false) }

    LaunchedEffect(isError) {
        showError = isError
    }

    var showPassword by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }

    STSYNTExtField(
        value = text,
        label = { Text(text = hint) },
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(40.dp),
        shape = RoundedCornerShape(cornerRadius),
        maxLines = 1,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = if (showError) errorColor else borderColor,
            unfocusedBorderColor = if (showError) errorColor else borderColor,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onSubmit(text)
            }
        ),
        onValueChange = {
            text = it
            onValueChange(it)
        },
        visualTransformation = if (showPassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }, trailingIcon = {

            if (showVisibilityIcon) {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null
                    )
                }
            } else showPassword = true
        }
    )
}