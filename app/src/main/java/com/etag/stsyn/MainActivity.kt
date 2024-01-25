package com.etag.stsyn

import android.content.pm.ActivityInfo
import android.os.Bundle
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
import androidx.work.WorkManager
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.receiver.BluetoothReceiverViewModel
import com.etag.stsyn.core.receiver.BluetoothState
import com.etag.stsyn.data.worker.TokenRefresher
import com.etag.stsyn.ui.navigation.NavigationGraph
import com.etag.stsyn.ui.screen.login.LoginViewModel
import com.etag.stsyn.ui.theme.STSYNTheme
import com.etag.stsyn.util.PermissionUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var workManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // lock screen rotation
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        workManager = WorkManager.getInstance(this)

        setContent {
            val navController = rememberNavController()
            val loginViewModel: LoginViewModel = hiltViewModel()
            val showAuthorizationFailedDialog by loginViewModel.showAuthorizationFailedDialog.collectAsState()
            val bluetoothReceiverViewModel: BluetoothReceiverViewModel = hiltViewModel()
            val bluetoothState by bluetoothReceiverViewModel.bluetoothState.collectAsState()
            val context = LocalContext.current
            val user by loginViewModel.savedUser.collectAsState()

            PermissionUtil.checkBluetoothPermission(context)

            // connect reader only when the app starts
            LaunchedEffect(Unit) {
                loginViewModel.connectReader()

                // refresh token every 45 minutes
                TokenRefresher.refresh(workManager)

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

    override fun onDestroy() {
        super.onDestroy()
        workManager.cancelAllWork() // cancel all token refresher when the app is destroyed
    }
}