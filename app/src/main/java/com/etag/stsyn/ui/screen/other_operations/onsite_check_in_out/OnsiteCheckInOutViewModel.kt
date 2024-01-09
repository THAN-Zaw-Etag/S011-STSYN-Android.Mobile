package com.etag.stsyn.ui.screen.other_operations.onsite_check_in_out

import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnsiteCheckInOutViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler
) : BaseViewModel(rfidHandler) {
    override fun onReceivedTagId(id: String) {

    }
}