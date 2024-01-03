package com.etag.stsyn.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Viewmodel for reader actions */

@HiltViewModel
class RfidViewModel @Inject constructor(
    private val rfidHandler: ZebraRfidHandler
) : BaseViewModel(rfidHandler) {

    private var reconnectingJob: Job? = null

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

    override fun onReceivedTagId(id: String) {
        TODO("Not yet implemented")
    }
}

