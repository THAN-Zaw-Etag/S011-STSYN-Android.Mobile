package com.etag.stsyn.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.reader.RfidBatteryLevelListener
import com.etag.stsyn.core.reader.RfidResponseHandlerInterface
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.tzh.retrofit_module.util.ApiResponse
import com.zebra.rfid.api3.TagData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel(
    private val rfidHandler: ZebraRfidHandler,
    private val TAG: String = "Base Viewmodel"
) : ViewModel(), RfidResponseHandlerInterface {

    private val _rfidUiState = MutableStateFlow(RfidUiState())
    val rfidUiState: StateFlow<RfidUiState> = _rfidUiState.asStateFlow()

    private val _detailUiState = MutableStateFlow(DetailUiState())
    val detailUiState: StateFlow<DetailUiState> = _detailUiState.asStateFlow()

    private val _showAuthorizationFailedDialog = MutableStateFlow(false)
    val showAuthorizationFailedDialog: StateFlow<Boolean> = _showAuthorizationFailedDialog.asStateFlow()

    private val _eventFlow = MutableSharedFlow<ClickEvent>()
    val eventFlow: SharedFlow<ClickEvent> = _eventFlow.asSharedFlow()

    private var reconnectingJob: Job? = null

    init {
        setRfidListener()
        updateScanType(ScanType.Multi)
    }

    fun shouldShowAuthorizationFailedDialog(isVisible: Boolean) {
        _showAuthorizationFailedDialog.update { isVisible }
    }

    fun updateIsSavedStatus(isSaved: Boolean) {
        _detailUiState.update {
            it.copy(isSaved = isSaved)
        }
    }

    fun updateSuccessDialogVisibility(visible: Boolean){
        _detailUiState.update { it.copy(showSuccessDialog = visible) }
    }

    protected fun toggleLoadingVisibility(visible: Boolean) {
        _detailUiState.update {
            it.copy(showLoadingDialog = visible)
        }
    }

    /** @param response is a response from api.
     * Handle api response state and update loading , error dialogs states.
     * @see delay not to show loading dialog immediately
     * @see disableScan disable scan while loading*/
    protected suspend fun <T> handleDialogStatesByResponse(response: ApiResponse<T>) {
        when (response) {
            is ApiResponse.Loading -> {
                delay(500)
                toggleLoadingVisibility(true)
                disableScan()
            }
            is ApiResponse.Success -> {
                toggleLoadingVisibility(false)
                enableScan()
            }
            is ApiResponse.ApiError -> {
                toggleLoadingVisibility(false)
                updateErrorMessage(response.message)
            }
            is ApiResponse.AuthorizationError -> {
                toggleLoadingVisibility(false)
                shouldShowAuthorizationFailedDialog(true)
            }
            else -> {}
        }
    }

    fun updateErrorMessage(message: String) {
        _detailUiState.update { it.copy(message = message) }
    }

    fun updateScanType(scanType: ScanType) {
        _rfidUiState.update {
            it.copy(scanType = scanType)
        }
    }

    fun removeItem(item: String) {
        val currentItems = _rfidUiState.value.scannedItems.toMutableList()
        currentItems.remove(item)
        _rfidUiState.update { it.copy(scannedItems = currentItems) }
    }

    fun removeScannedItems() {
        _rfidUiState.update {
            it.copy(scannedItems = mutableListOf())
        }
    }

    // to initialize reader when needed
    fun onCreate() {
        viewModelScope.launch { rfidHandler.onCreate() }
    }

    fun connectReader() {
        if (reconnectingJob?.isActive == true) {
            return
        }
        viewModelScope.launch {
            try {
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

                    updateIsConnectedStatus(rfidHandler.isReaderConnected)
                }
                getReaderBatteryLevel()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setRfidListener() {
        rfidHandler.setResponseHandlerInterface(this)
    }

    /**
     * Remove rfid listener
     * */
    fun removeListener() {
        rfidHandler.removeResponseHandLerInterface()
    }

    fun updateIsScanningStatus(isScanning: Boolean) {
        _rfidUiState.update {
            // update value only when reader is connected
            it.copy(isScanning = if (it.isConnected) isScanning else it.isScanning)
        }
    }

    fun toggle() {
        if (_rfidUiState.value.isScanning) stopScan()
        else startScan()
    }

    private fun startScan() {
        viewModelScope.launch {
            // only able to scan when isScannable is true
            if (_rfidUiState.value.isScannable) {
                updateIsScanningStatus(true)
                rfidHandler.performInventory()
            }
        }
    }

    fun stopScan() {
        viewModelScope.launch {
            updateIsScanningStatus(false)
            rfidHandler.stopInventory()
        }
    }

    /**
     * Disable reader scan*/
    fun disableScan() {
        updateIsScannableStatus(false)
    }

    fun enableScan() {
        updateIsScannableStatus(true)
    }

    private fun updateIsScannableStatus(isScannable: Boolean) {
        _rfidUiState.update {
            it.copy(isScannable = isScannable)
        }
    }

    fun updateIsConnectedStatus(isConnected: Boolean) {
        _rfidUiState.update { it.copy(isConnected = isConnected) }
    }


    private fun getReaderBatteryLevel() {
        rfidHandler.setOnBatteryLevelListener(object : RfidBatteryLevelListener {
            override fun onBatteryLevelChange(batteryLevel: Int) {
                _rfidUiState.update {
                    it.copy(batteryLevel = batteryLevel, isConnected = true)
                }
            }
        }
        )
    }

    abstract fun onReceivedTagId(id: String)
    override fun handleTagData(tagData: Array<TagData>) {
        if (_rfidUiState.value.scanType == ScanType.Single) {
            if (tagData.get(0).tagID.isNotEmpty()) stopScan()
        }
        onReceivedTagId(tagData.get(0).tagID)
    }

    /**
     * @param event is type of click event.
     *
     * For general function for its children.
     * This function is used in detail screen where is general screen for all screens.
     * When we invoke this function, we will do corresponding task in child viewmodel.
     * */
    fun updateClickEvent(event: ClickEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    override fun handleReaderConnected(isConnected: Boolean) {
        updateIsConnectedStatus(isConnected)
        if (isConnected) getReaderBatteryLevel()
    }

    override fun handleTriggerPress(pressed: Boolean) {
        if (pressed && _rfidUiState.value.isScannable) startScan() else stopScan()
    }

    data class DetailUiState(
        val showLoadingDialog: Boolean = false,
        val showSuccessDialog: Boolean = false,
        val isSaved: Boolean = false,
        val message: String = ""
    )

    data class RfidUiState(
        val isScanning: Boolean = false,
        val isScannable: Boolean = true,
        val scanType: ScanType = ScanType.Multi,
        val scannedItems: List<String> = mutableListOf(),
        val singleScannedItem: String = "",
        val isConnected: Boolean = false,
        val isScanned: Boolean = false,
        val batteryLevel: Int = 0,
    )

    enum class ScanType {
        Single, Multi
    }

}
sealed class ClickEvent {
    object ClickAfterSave : ClickEvent()
    object RetryClick: ClickEvent()
    object Default: ClickEvent()
}
