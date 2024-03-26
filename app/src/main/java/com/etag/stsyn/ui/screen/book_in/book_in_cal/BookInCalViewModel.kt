package com.etag.stsyn.ui.screen.book_in.book_in_cal

import android.util.Log
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookInCalViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler
) : BaseViewModel(rfidHandler) {
    companion object {
        const val TAG = "BookInCalViewModel"
    }
    override fun onReceivedTagId(id: String) {
    }
}