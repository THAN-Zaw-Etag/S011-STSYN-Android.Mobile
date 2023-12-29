package com.etag.stsyn.ui.viewmodel

import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.etag.stsyn.ui.navigation.Routes
import com.zebra.rfid.api3.TagData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SharedUiViewModel @Inject constructor(
    val rfidHandler: ZebraRfidHandler
) : BaseViewModel(rfidHandler) {
    private val _uiState = MutableStateFlow(SharedUiState())
    val uiState: StateFlow<SharedUiState> = _uiState.asStateFlow()

    private val _isReaderConnected = MutableStateFlow(false)
    val isReaderConnected: StateFlow<Boolean> = _isReaderConnected.asStateFlow()

    /*private var reconnectingJob: Job? = null
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
        }
    }*/

    fun updateTopAppBarStatus(show: Boolean) {
        _uiState.update {
            it.copy(showTopAppBar = show)
        }
    }

    fun updateBottomNavigationSelectedItem(title: String) {
        _uiState.update {
            it.copy(selectedBottomNavigationItem = title)
        }
    }

    fun updateTopBarTitle(title: String) {
        _uiState.update {
            it.copy(title = title)
        }
    }

    fun updateBottomNavigationBarStatus(showBottomNavigationBar: Boolean) {
        _uiState.update {
            it.copy(
                showBottomNavigationBar = showBottomNavigationBar
            )
        }
    }

    override fun handleTagData(tagData: Array<TagData>) {
        TODO("Not yet implemented")
    }

    override fun handleTriggerPress(pressed: Boolean) {
        TODO("Not yet implemented")
    }

    override fun handleReaderConnected(isConnected: Boolean) {
        updateConnectionStatus(isConnected)
    }
}

data class SharedUiState(
    val title: String = Routes.HomeScreen.title,
    val showTopAppBar: Boolean = true,
    val selectedBottomNavigationItem: String = Routes.HomeScreen.title,
    val showBottomNavigationBar: Boolean = false
)