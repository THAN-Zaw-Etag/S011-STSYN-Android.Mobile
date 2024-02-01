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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.receiver.BluetoothReceiverViewModel
import com.etag.stsyn.core.receiver.BluetoothState
import com.etag.stsyn.data.worker.TokenRefresher
import com.etag.stsyn.ui.screen.login.LoginViewModel
import com.etag.stsyn.ui.theme.STSYNTheme
import com.etag.stsyn.ui.navigation.RootNavigationGraph
import com.etag.stsyn.util.PermissionUtil
import com.tzh.retrofit_module.data.model.LocalUser
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
            val loginUiState by loginViewModel.loginUiState.collectAsState()
            val bluetoothReceiverViewModel: BluetoothReceiverViewModel = hiltViewModel()
            val bluetoothState by bluetoothReceiverViewModel.bluetoothState.collectAsState()
            val savedUser by loginViewModel.savedUser.collectAsState(LocalUser())
            val context = LocalContext.current

            installSplashScreen().setKeepOnScreenCondition {
                loginViewModel.loading.value
            }

            workManager.getWorkInfosByTagLiveData("refreshWorkName")
                .observe(LocalLifecycleOwner.current) {
                    val workInfo = it.firstOrNull { it.state.isFinished }
                    if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                        val outputData = workInfo.outputData
                        val outputValue = outputData.getString("api_token")
                        loginViewModel.saveToken(outputValue!!)
                        Log.d("TAG", "doWork: savedToken: $outputValue")
                    }
                }

            PermissionUtil.checkBluetoothPermission(context)

            // connect reader only when the app starts
            LaunchedEffect(Unit) {
                loginViewModel.connectReader()

                // refresh token every 45 minutes if only login is successful
                if (loginUiState.isLoginSuccessful) {
                    TokenRefresher.refresh(workManager)
                }
            }

            handleBluetoothState(bluetoothState = bluetoothState, loginViewModel = loginViewModel)

            STSYNTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
//                    NavigationGraph(
//                        navController = navController,
//                        loginViewModel = loginViewModel,
//                    )

                    Log.d("@inMain", "MainActivity: savedUser: ${savedUser.isLoggedIn}")
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
        workManager.cancelAllWork() // cancel all token refresher when the app is destroyed
    }
}