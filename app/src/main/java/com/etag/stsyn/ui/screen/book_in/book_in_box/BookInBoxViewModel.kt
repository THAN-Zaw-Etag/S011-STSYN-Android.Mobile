package com.etag.stsyn.ui.screen.book_in.book_in_box

import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.zebra.rfid.api3.TagData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookInBoxViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler
) : BaseViewModel(rfidHandler) {
    override fun onReceivedTagId(id: String) {
        TODO("Not yet implemented")
    }

    override fun handleTagData(tagData: Array<TagData>) {
        TODO("Not yet implemented")
    }

    override fun handleTriggerPress(pressed: Boolean) {
        TODO("Not yet implemented")
    }

    override fun handleReaderConnected(isConnected: Boolean) {
        TODO("Not yet implemented")
    }
}