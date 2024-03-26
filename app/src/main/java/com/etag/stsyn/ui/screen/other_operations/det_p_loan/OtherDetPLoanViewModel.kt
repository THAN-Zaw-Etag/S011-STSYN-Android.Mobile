package com.etag.stsyn.ui.screen.other_operations.det_p_loan

import android.util.Log
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OtherDetPLoanViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler
) : BaseViewModel(rfidHandler) {
    companion object {
        const val TAG = "OtherDetPLoanViewModel"
    }
    override fun onReceivedTagId(id: String) {
    }
}