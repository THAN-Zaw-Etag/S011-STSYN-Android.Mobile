package com.etag.stsyn

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.etag.stsyn.core.receiver.BluetoothReceiverViewModel
import com.etag.stsyn.core.receiver.BluetoothState
import com.etag.stsyn.ui.navigation.NavigationGraph
import com.etag.stsyn.ui.screen.login.LoginViewModel
import com.etag.stsyn.ui.theme.STSYNTheme
import com.etag.stsyn.util.PermissionUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var rfidHandler: ZebraRfidHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // lock screen rotation
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {
            val navController = rememberNavController()
            val loginViewModel: LoginViewModel = hiltViewModel()
            val showAuthorizationFailedDialog by loginViewModel.showAuthorizationFailedDialog.collectAsState()
            val bluetoothReceiverViewModel: BluetoothReceiverViewModel = hiltViewModel()
            val bluetoothState by bluetoothReceiverViewModel.bluetoothState.collectAsState()
            val savedUser by loginViewModel.savedUser.collectAsState()
            val context = LocalContext.current

            PermissionUtil.checkBluetoothPermission(context)

            loginViewModel.refreshToken(this)

            // connect reader only when the app starts
            LaunchedEffect(Unit) {
                loginViewModel.connectReader()
                loginViewModel.savedUser.collect {
                    Log.d("TAG", "appToken: ${it.token}")
                }
            }

            handleBluetoothState(bluetoothState = bluetoothState, loginViewModel = loginViewModel)


            STSYNTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    NavigationGraph(
                        navController = navController,
                        loginViewModel = loginViewModel,
                    )
                }
            }
        }
    }

    private fun handleBluetoothState(
        bluetoothState: BluetoothState,
        loginViewModel: BaseViewModel
    ) {
        when (bluetoothState) {
            BluetoothState.ON -> {
                loginViewModel.apply {
                    connectReader()
                }
            }

            BluetoothState.OFF, BluetoothState.TURNING_OFF -> {
                loginViewModel.updateIsConnectedStatus(false)
            }

            else -> {}
        }
    }
}