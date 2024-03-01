@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen.settings

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NotListedLocation
import androidx.compose.material.icons.filled.AdfScanner
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.etag.stsyn.ui.components.DropDown
import com.etag.stsyn.util.AppUtil
import com.tzh.retrofit_module.data.settings.AppConfigModel
import com.tzh.retrofit_module.data.settings.StoreType

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val appConfiguration by viewModel.appConfig.collectAsState(initial = AppConfigModel())
    var needLocation by remember { mutableStateOf(false) }

    LaunchedEffect(appConfiguration.needLocation) {
        needLocation = appConfiguration.needLocation
    }
    var baseUrlStatus by remember { mutableStateOf(false) }

    LaunchedEffect(baseUrlStatus) {
        if (baseUrlStatus) Toast.makeText(context, "Enter valid Url!", Toast.LENGTH_SHORT)
            .show()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        SettingItem(
            icon = Icons.Default.AdfScanner,
            title = "Reader Id",
            value = appConfiguration.handheldReaderId,
            description = appConfiguration.handheldReaderId,
            onUpdateClick = { viewModel.updateAppConfig(appConfiguration.copy(handheldReaderId = it)) }
        )
        SettingItem(
            icon = Icons.Default.Numbers,
            title = "Serial No",
            value = appConfiguration.handheldReaderSerialNo,
            description = appConfiguration.handheldReaderSerialNo,
            onUpdateClick = { viewModel.updateAppConfig(appConfiguration.copy(handheldReaderSerialNo = it)) }
        )

        SettingItem(
            icon = Icons.Default.Link,
            title = "API Url",
            value = appConfiguration.apiUrl,
            description = appConfiguration.apiUrl,
            onUpdateClick = {
                baseUrlStatus = if (AppUtil.baseUrlValidationStatus(it)) {
                    viewModel.updateAppConfig(appConfiguration.copy(apiUrl = it))
                    viewModel.updateBaseUrl(it)
                    false
                }else{
                    true
                }

            }
        )

        SettingItem(
            icon = Icons.Default.Speed,
            title = "Power",
            value = appConfiguration.power.toString(),
            description = appConfiguration.power.toString(),
            onUpdateClick = { viewModel.updateAppConfig(appConfiguration.copy(power = it.toLong())) }
        )

        SettingItem(
            icon = Icons.Default.FormatListNumbered,
            title = "CS No",
            value = appConfiguration.csNo,
            description = appConfiguration.csNo,
            onUpdateClick = { viewModel.updateAppConfig(appConfiguration.copy(csNo = it)) }
        )

        SettingItem(
            icon = Icons.Default.Store,
            title = "Store Type",
            description = "Choose store type",
            clickable = false,
            trailingIcon = {
                DropDown(
                    modifier = Modifier.fillMaxWidth(0.5f),
                    items = StoreType.entries.map { it.toString() },
                    defaultValue = appConfiguration.store.toString(),
                    onSelected = {
                        viewModel.updateAppConfig(
                            appConfiguration.copy(
                                store = StoreType.valueOf(
                                    it
                                )
                            )
                        )
                    }
                )
            }
        )

        SettingItem(
            icon = Icons.AutoMirrored.Filled.NotListedLocation,
            title = "Need Location",
            description = "Enable location if required",
            clickable = false,
            trailingIcon = {
                Switch(
                    checked = needLocation,
                    onCheckedChange = { viewModel.updateAppConfig(appConfiguration.copy(needLocation = it)) })
            }
        )
    }
}

@Composable
private fun UpdateSettingItemDialog(
    value: String,
    onDismiss: () -> Unit,
    onSubmitClick: (String) -> Unit
) {
    var inputValue by remember { mutableStateOf(value) }
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        Dialog(
            onDismissRequest = {
                showDialog = false
                onDismiss()
            }) {
            Surface(
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Edit",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    val containerColor = MaterialTheme.colorScheme.primary.copy(0.1f)
                    TextField(
                        value = inputValue,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = containerColor,
                            unfocusedContainerColor = containerColor,
                            disabledContainerColor = containerColor,
                        ),
                        onValueChange = { inputValue = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    FilledTonalButton(
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            showDialog = false
                            onSubmitClick(inputValue)
                        }) {
                        Text(text = "Update")
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    value: String = "",
    description: String,
    clickable: Boolean = true,
    onUpdateClick: (String) -> Unit = {},
    trailingIcon: @Composable () -> Unit = {}
) {
    var showEditDialog by remember { mutableStateOf(false) }
    val clickableModifier = modifier
        .clickable { showEditDialog = true }
        .fillMaxWidth()
        .padding(16.dp)
    val unClickableModifier = modifier
        .fillMaxWidth()
        .padding(16.dp)

    if (showEditDialog) {
        UpdateSettingItemDialog(
            value = value,
            onSubmitClick = onUpdateClick,
            onDismiss = { showEditDialog = false })
    }

    Column {
        Row(
            modifier = if (clickable) clickableModifier else unClickableModifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.Gray)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleLarge, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                AnimatedVisibility(visible = description.isNotEmpty()) {
                    Text(text = description, color = Color.Gray, fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            trailingIcon()
        }
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
    }
}