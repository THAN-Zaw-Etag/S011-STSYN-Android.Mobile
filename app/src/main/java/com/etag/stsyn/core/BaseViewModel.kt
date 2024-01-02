package com.etag.stsyn.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.reader.RfidResponseHandlerInterface
import com.etag.stsyn.core.reader.ZebraRfidHandler
import kotlinx.coroutines.launch


abstract class BaseViewModel(
    private val rfidHandler: ZebraRfidHandler
) : ViewModel(), RfidResponseHandlerInterface {

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
}