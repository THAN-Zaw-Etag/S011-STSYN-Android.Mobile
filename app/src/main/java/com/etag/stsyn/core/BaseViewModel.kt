package com.etag.stsyn.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.reader.RfidBatteryLevelListener
import com.etag.stsyn.core.reader.RfidResponseHandlerInterface
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.zebra.rfid.api3.TagData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


abstract class BaseViewModel(
    private val rfidHandler: ZebraRfidHandler,
) : ViewModel(), RfidResponseHandlerInterface {

    private val _isScanning = MutableStateFlow(false)
    var isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val _scannedItems = MutableStateFlow(mutableListOf<String>())
    val scannedItems: StateFlow<List<String>> = _scannedItems.asStateFlow()

    private val _isMultiScanning = MutableStateFlow(false)
    val isMultiScanning: StateFlow<Boolean> = _isMultiScanning.asStateFlow()

    private val _hasScanned = MutableStateFlow(false)
    val hasScanned: StateFlow<Boolean> = _hasScanned.asStateFlow()

    private val _rfidUiState = MutableStateFlow(RfidUiState())
    val rfidUiState: StateFlow<RfidUiState> = _rfidUiState.asStateFlow()

    init {
        updateMultiScanningStatus(false)
    }

    fun updateMultiScanningStatus(isMultiScanning: Boolean) {
        _isMultiScanning.value = isMultiScanning
    }

    fun addScannedItem(item: String) {
        val hasExisted = item in _scannedItems.value
        if (!hasExisted) _scannedItems.value.add(item)
    }

    fun removeScannedItem(item: String) {
        _scannedItems.value.remove(item)
    }

    // to initialize reader when needed
    fun onCreate() {
        viewModelScope.launch { rfidHandler.onCreate() }
    }

    fun setRfidListener() {
        rfidHandler.setResponseHandlerInterface(this)
    }

    fun toggle() {
        if (_isScanning.value) stopScan()
        else startScan()
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

    init {
        getReaderBatteryLevel()
    }

    fun updateConnectionStatus(isConnected: Boolean) {
        _rfidUiState.update {
            it.copy(isConnected = isConnected)
        }
    }

    fun updateIsScannedStatus(isScanned: Boolean) {
        _rfidUiState.update { it.copy(isScanned = isScanned) }
    }

    protected fun updateIsConnectedStatus(isConnected: Boolean) {
        _rfidUiState.update { it.copy(isConnected = isConnected) }
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

    abstract fun onReceivedTagId(id: String)
    override fun handleTagData(tagData: Array<TagData>) {
        if (tagData.isNotEmpty() && false) stopScan()
        addScannedItem(tagData.get(0).tagID)

        onReceivedTagId(tagData.get(0).tagID)

        _hasScanned.value = tagData.isNotEmpty()
    }

    override fun handleReaderConnected(isConnected: Boolean) {
        updateIsConnectedStatus(isConnected)
    }

    override fun handleTriggerPress(pressed: Boolean) {
        if (pressed) startScan() else stopScan()
    }

    data class RfidUiState(
        val isConnected: Boolean = false,
        val isScanned: Boolean = false,
        val batteryLevel: Int = 0,
    )
}