package com.etag.stsyn.ui.screen.book_in.t_loan

import android.util.Log
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TLoanViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler
) : BaseViewModel(rfidHandler) {
    companion object {
        const val TAG = "TLoanViewModel"
    }

    override fun onReceivedTagId(id: String) {

    }

}