package com.etag.stsyn.ui.screen.other_operations.det_p_loan_box

import android.util.Log
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.zebra.rfid.api3.TagData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OtherDetPLoanBoxViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler
) : BaseViewModel(rfidHandler) {
    companion object {
        const val TAG = "OtherDetPLoanBoxViewModel"
    }
    override fun handleTagData(tagData: Array<TagData>) {
        Log.d(TAG, "handleTagData")
    }

    override fun onReceivedTagId(id: String) {
        Log.d(TAG, "onReceivedTagId")
    }

    override fun handleTriggerPress(pressed: Boolean) {
        Log.d(TAG, "handleTriggerPress")
    }

    override fun handleReaderConnected(isConnected: Boolean) {
        Log.d(TAG, "handleReaderConnected")
    }
}