package com.etag.stsyn.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.reader.RfidResponseHandlerInterface
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.zebra.rfid.api3.TagData
import kotlinx.coroutines.launch

abstract class BaseRFIDViewModel(
    val rfidHandler: ZebraRfidHandler,
) : ViewModel(), RfidResponseHandlerInterface {


    fun setListener() {
        rfidHandler.setResponseHandlerInterface(this)
    }

    fun removeListener() {
        rfidHandler.removeResponseHandLerInterface()
    }

    fun startScan() {
        viewModelScope.launch { rfidHandler.performInventory() }
    }

    fun stopScan() {
        viewModelScope.launch { rfidHandler.stopInventory() }
    }

    abstract fun scanTag(rfid: String)

    override fun handleTagData(tagData: Array<TagData>) {
        scanTag(tagData[0].tagID)
    }

    override fun handleTriggerPress(pressed: Boolean) {
        if (pressed) {
            startScan()
        } else {
            stopScan()
        }
    }

    override fun handleReaderConnected(isConnected: Boolean) {

    }
}