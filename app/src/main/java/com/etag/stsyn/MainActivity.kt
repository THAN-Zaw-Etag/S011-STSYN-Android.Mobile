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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.etag.stsyn.core.receiver.BluetoothReceiverViewModel
import com.etag.stsyn.core.receiver.BluetoothState
import com.etag.stsyn.data.worker.TokenRefreshWorker
import com.etag.stsyn.ui.navigation.NavigationGraph
import com.etag.stsyn.ui.screen.login.LoginViewModel
import com.etag.stsyn.ui.theme.STSYNTheme
import com.etag.stsyn.util.PermissionUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
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

            refreshToken()

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

    private fun refreshToken() {
        Log.d("TAG", "doWork: call refresh token")
        val periodicRefreshRequest = PeriodicWorkRequest.Builder(
            TokenRefreshWorker::class.java,
            5,
            TimeUnit.SECONDS
        ).build()


        /*WorkManager.getInstance().enqueueUniquePeriodicWork(
            "1023",
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicRefreshRequest.build()
        )
        val oneTimeWorker = OneTimeWorkRequestBuilder<TokenRefreshWorker>()
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build() */

        val workManager = WorkManager.getInstance(this)
        workManager.enqueueUniquePeriodicWork(
            "1023",
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicRefreshRequest
        )

        val workStatus = workManager.getWorkInfoByIdLiveData(periodicRefreshRequest.id)
        workStatus.observe(this, Observer {
            if (it != null && it.state.isFinished) {
                Log.d("TAG", "doWork: work done")
                //workManager.cancelWorkById(periodicRefreshRequest.id)
            }
        })
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