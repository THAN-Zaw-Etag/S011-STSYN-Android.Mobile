package com.etag.stsyn.ui.screen.book_in.book_in

import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.zebra.rfid.api3.TagData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookInViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler
) : BaseViewModel(rfidHandler) {
    override fun handleTagData(tagData: Array<TagData>) {

    }

    override fun handleTriggerPress(pressed: Boolean) {

    }

    override fun handleReaderConnected(isConnected: Boolean) {

    }

}