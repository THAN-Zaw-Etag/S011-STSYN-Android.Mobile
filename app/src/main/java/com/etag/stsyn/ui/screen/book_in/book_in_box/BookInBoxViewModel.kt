package com.etag.stsyn.ui.screen.book_in.book_in_box

import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookInBoxViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler
) : BaseViewModel(rfidHandler) {
    override fun onReceivedTagId(id: String) {

    }
}