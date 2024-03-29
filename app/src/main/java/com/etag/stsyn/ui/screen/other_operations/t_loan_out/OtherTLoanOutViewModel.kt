package com.etag.stsyn.ui.screen.other_operations.t_loan_out

import android.util.Log
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OtherTLoanOutViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler
) : BaseViewModel(rfidHandler) {
    companion object {
        const val TAG = "OtherTLoanOutViewModel"
    }
    override fun onReceivedTagId(id: String) {
    }
}