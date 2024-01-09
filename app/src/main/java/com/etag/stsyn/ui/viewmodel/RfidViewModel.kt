package com.etag.stsyn.ui.viewmodel

import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Viewmodel for reader actions */

@HiltViewModel
class RfidViewModel @Inject constructor(
    private val rfidHandler: ZebraRfidHandler
) : BaseViewModel(rfidHandler) {


    override fun onReceivedTagId(id: String) {

    }
}

