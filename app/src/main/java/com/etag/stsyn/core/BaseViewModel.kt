package com.etag.stsyn.core

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.reader.RfidBatteryLevelListener
import com.etag.stsyn.core.reader.RfidResponseHandlerInterface
import com.etag.stsyn.core.reader.ZebraRfidHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


abstract class BaseViewModel(
    private val rfidHandler: ZebraRfidHandler
) : ViewModel(), RfidResponseHandlerInterface {

    private var isStartScanning by mutableStateOf(false)
    private var reconnectingJob: Job? = null

    private val _rfidUiState = MutableStateFlow(RfidUiState())
    val rfidUiState: StateFlow<RfidUiState> = _rfidUiState.asStateFlow()

    init {
        viewModelScope.launch { rfidHandler.onCreate() }
        getReaderBatteryLevel()
    }

    fun updateConnectionStatus(isConnected: Boolean) {
        _rfidUiState.update {
            it.copy(isConnected = isConnected)
        }
    }

    fun updateScanningStatus(isScanning: Boolean) {
        _rfidUiState.update {
            it.copy(isStartScanning = isScanning)
        }
    }

    fun toggleScan() {
        if (_rfidUiState.value.isStartScanning) stopScan()
        else startScan()
    }

    fun connectReader() {
        if (reconnectingJob?.isActive == true) {
            return
        }
        viewModelScope.launch {
            reconnectingJob?.cancel()
            if (!rfidHandler.isReaderConnected) {
                reconnectingJob = rfidHandler.onCreate()
            }
            reconnectingJob?.invokeOnCompletion {
                if (!rfidHandler.isReaderConnected) {
                    viewModelScope.launch {
                        rfidHandler.onCreate()
                    }
                }
            }

            updateConnectionStatus(rfidHandler.isReaderConnected)
        }
    }

    private fun getReaderBatteryLevel() {
        rfidHandler.setOnBatteryLevelListener(object : RfidBatteryLevelListener {
            override fun onBatteryLevelChange(batteryLevel: Int) {
                _rfidUiState.update {
                    it.copy(batteryLevel = batteryLevel)
                }
            }
        })
    }

    fun setRfidListener() {
        rfidHandler.setResponseHandlerInterface(this)
    }

    fun removeListener() {
        stopScan()
        rfidHandler.removeResponseHandLerInterface()
    }

    fun startScan() {
        viewModelScope.launch {
            rfidHandler.performInventory()
            updateScanningStatus(true)
        }
    }

    fun stopScan() {
        viewModelScope.launch {
            rfidHandler.stopInventory()
            updateScanningStatus(false)
        }
    }
}

data class RfidUiState(
    val isConnected: Boolean = false,
    val batteryLevel: Int = 0,
    val isStartScanning: Boolean = false
)