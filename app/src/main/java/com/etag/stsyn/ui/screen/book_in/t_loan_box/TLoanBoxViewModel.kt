package com.etag.stsyn.ui.screen.book_in.t_loan_box

import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.zebra.rfid.api3.TagData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TLoanBoxViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler
) : BaseViewModel(rfidHandler) {
    override fun handleTagData(tagData: Array<TagData>) {

    }

    override fun onReceivedTagId(id: String) {

    }

    override fun handleTriggerPress(pressed: Boolean) {

    }

    override fun handleReaderConnected(isConnected: Boolean) {

    }
}