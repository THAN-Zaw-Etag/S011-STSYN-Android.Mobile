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

    init {
        viewModelScope.launch { rfidHandler.onCreate() }
    }

    fun setRfidListener() {
        rfidHandler.setResponseHandlerInterface(this)
    }

    fun removeListener() {
        rfidHandler.removeResponseHandLerInterface()
    }
}