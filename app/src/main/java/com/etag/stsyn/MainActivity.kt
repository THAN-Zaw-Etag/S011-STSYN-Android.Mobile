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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import androidx.work.WorkManager
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.receiver.BluetoothReceiverViewModel
import com.etag.stsyn.core.receiver.BluetoothState
import com.etag.stsyn.data.worker.TokenRefreshWorker
import com.etag.stsyn.data.worker.TokenRefresher
import com.etag.stsyn.ui.navigation.RootNavigationGraph
import com.etag.stsyn.ui.screen.login.LoginViewModel
import com.etag.stsyn.ui.theme.STSYNTheme
import com.etag.stsyn.util.PermissionUtil
import com.tzh.retrofit_module.data.model.LocalUser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    /*@Inject
    private lateinit var workManager: WorkManager*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // lock screen rotation
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        TokenRefreshWorker.refresh(this)

        setContent {
            val navController = rememberNavController()
            val loginViewModel: LoginViewModel = hiltViewModel()
            val bluetoothReceiverViewModel: BluetoothReceiverViewModel = hiltViewModel()
            val bluetoothState by bluetoothReceiverViewModel.bluetoothState.collectAsStateWithLifecycle()
            val savedUser by loginViewModel.savedUser.collectAsStateWithLifecycle(LocalUser())
            val context = LocalContext.current

            installSplashScreen().setKeepOnScreenCondition {
                loginViewModel.loading.value
            }


            // connect reader only when the app starts
            LaunchedEffect(Unit) {
                loginViewModel.connectReader()
            }

            PermissionUtil.checkBluetoothPermission(context)

            handleBluetoothState(bluetoothState = bluetoothState, loginViewModel = loginViewModel)

            STSYNTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val isLoggedIn = savedUser.isLoggedIn
                    RootNavigationGraph(
                        isLoggedIn = isLoggedIn,
                        navController = navController,
                        loginViewModel = loginViewModel

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
        //workManager.cancelAllWork() // cancel all token refresher when the app is destroyed
    }
}