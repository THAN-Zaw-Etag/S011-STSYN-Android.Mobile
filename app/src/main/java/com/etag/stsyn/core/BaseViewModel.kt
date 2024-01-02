package com.etag.stsyn.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.reader.RfidResponseHandlerInterface
import com.etag.stsyn.core.reader.ZebraRfidHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


abstract class BaseViewModel(
    private val rfidHandler: ZebraRfidHandler
) : ViewModel(), RfidResponseHandlerInterface {

    private val _isScanning = MutableStateFlow(false)
    var isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    // to initialize reader when needed
    fun onCreate() {
        viewModelScope.launch { rfidHandler.onCreate() }
    }

    fun setRfidListener() {
        rfidHandler.setResponseHandlerInterface(this)
    }

    fun removeListener() {
        rfidHandler.removeResponseHandLerInterface()
    }

    fun startScan() {
        viewModelScope.launch {
            rfidHandler.performInventory()
            _isScanning.value = true
        }
    }

    fun stopScan() {
        viewModelScope.launch {
            rfidHandler.stopInventory()
            _isScanning.value = false
        }
    }
}