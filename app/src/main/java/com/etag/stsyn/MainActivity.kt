package com.etag.stsyn

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
import com.etag.ReaderLifeCycle
import com.etag.stsyn.core.receiver.BluetoothReceiverViewModel
import com.etag.stsyn.core.receiver.BluetoothState
import com.etag.stsyn.ui.navigation.NavigationGraph
import com.etag.stsyn.ui.theme.STSYNTheme
import com.etag.stsyn.ui.viewmodel.RfidViewModel
import com.etag.stsyn.ui.viewmodel.SharedUiViewModel
import com.etag.stsyn.util.PermissionUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val sharedUiViewModel: SharedUiViewModel = hiltViewModel()
            val rfidViewModel: RfidViewModel = hiltViewModel()
            val bluetoothReceiverViewModel: BluetoothReceiverViewModel = hiltViewModel()

            val bluetoothState by bluetoothReceiverViewModel.bluetoothState.collectAsState()

            val context = LocalContext.current

            PermissionUtil.checkBluetoothPermission(context)

            // initialize reader
            LaunchedEffect(Unit) {
                rfidViewModel.onCreate()
            }

            when (bluetoothState) {
                BluetoothState.ON -> {
                    rfidViewModel.connectReader()
                }

                else -> {}
            }

            val navController = rememberNavController()

            STSYNTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    ReaderLifeCycle(viewModel = rfidViewModel)
                    NavigationGraph(
                        navController = navController,
                        rfidViewModel = rfidViewModel,
                        sharedUiViewModel = sharedUiViewModel
                    )
                }
            }
        }
    }
}