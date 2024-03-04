package com.etag.stsyn.ui.screen.book_in.det_p_loan

import android.util.Log
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetPLoanViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler
) : BaseViewModel(rfidHandler) {
    companion object {
        const val TAG = "DetPLoanViewModel"
    }
    override fun onReceivedTagId(id: String) {
        Log.d(TAG, "onReceivedTagId: $id")
    }
}