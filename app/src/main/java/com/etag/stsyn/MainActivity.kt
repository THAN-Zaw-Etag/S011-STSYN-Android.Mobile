package com.etag.stsyn

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import androidx.work.WorkManager
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.receiver.BluetoothReceiverViewModel
import com.etag.stsyn.core.receiver.BluetoothState
import com.etag.stsyn.data.worker.TokenRefreshWorker
import com.etag.stsyn.ui.navigation.RootNavigationGraph
import com.etag.stsyn.ui.screen.login.LoginViewModel
import com.etag.stsyn.ui.theme.STSYNTheme
import com.etag.stsyn.ui.viewmodel.SharedUiViewModel
import com.etag.stsyn.util.PermissionUtil
import com.tzh.retrofit_module.data.model.LocalUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var workManager: WorkManager

    companion object {
        const val TAG = "MainActivity"
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // lock screen rotation
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        workManager = WorkManager.getInstance(this)
        TokenRefreshWorker.refresh(workManager)

        setContent {
            val navController = rememberNavController()
            val loginViewModel: LoginViewModel = hiltViewModel()
            val sharedUiViewModel: SharedUiViewModel = hiltViewModel()
            val bluetoothReceiverViewModel: BluetoothReceiverViewModel = hiltViewModel()
            val bluetoothState by bluetoothReceiverViewModel.bluetoothState.collectAsStateWithLifecycle()
            val savedUser by loginViewModel.savedUser.collectAsStateWithLifecycle()
            val context = LocalContext.current

            PermissionUtil.checkBluetoothPermission(context)
            handleBluetoothState(bluetoothState = bluetoothState, loginViewModel = loginViewModel)

            installSplashScreen().setKeepOnScreenCondition {
                loginViewModel.loading.value
            }

            // cancel token refresher worker when user is logged out
            LaunchedEffect(savedUser) {
                if (!savedUser.isLoggedIn) TokenRefreshWorker.cancel(workManager)
            }

            // connect reader only when the app starts
            LaunchedEffect(Unit) {
                loginViewModel.connectReader()
            }

            STSYNTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    RootNavigationGraph(
                        isLoggedIn = savedUser.isLoggedIn,
                        navController = navController,
                        sharedUiViewModel = sharedUiViewModel,
                        loginViewModel = loginViewModel
                    )
                }
            }
        }
    }

    private fun handleBluetoothState(
        bluetoothState: BluetoothState,
        loginViewModel: LoginViewModel
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
        try {
            TokenRefreshWorker.cancel(workManager)
        }catch (_: Exception){}
    }
}