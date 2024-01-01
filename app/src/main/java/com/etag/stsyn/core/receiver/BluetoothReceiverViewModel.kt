package com.etag.stsyn.core.receiver

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class BluetoothReceiverViewModel @Inject constructor(private val application: Application) :
    ViewModel() {
    private val _bluetoothState = MutableStateFlow<BluetoothState>(BluetoothState.UNKNOWN)
    val bluetoothState: StateFlow<BluetoothState> = _bluetoothState.asStateFlow()

    private val bluetoothStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    val state =
                        intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                    _bluetoothState.value = when (state) {
                        BluetoothAdapter.STATE_OFF -> BluetoothState.OFF
                        BluetoothAdapter.STATE_TURNING_ON -> BluetoothState.TURNING_ON
                        BluetoothAdapter.STATE_ON -> BluetoothState.ON
                        BluetoothAdapter.STATE_TURNING_OFF -> BluetoothState.TURNING_OFF
                        else -> BluetoothState.UNKNOWN
                    }
                }
            }
        }
    }

    init {
        //registerBluetoothReceiver()
    }

    fun registerBluetoothReceiver() {
        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        application.registerReceiver(bluetoothStateReceiver, filter)
    }

    fun unregisterBluetoothReceiver() {
        application.unregisterReceiver(bluetoothStateReceiver)
    }

    override fun onCleared() {
        super.onCleared()
        unregisterBluetoothReceiver()
    }
}

enum class BluetoothState {
    ON,
    OFF,
    TURNING_ON,
    TURNING_OFF,
    UNKNOWN
}