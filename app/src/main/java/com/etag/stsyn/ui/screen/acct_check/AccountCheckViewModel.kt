package com.etag.stsyn.ui.screen.acct_check

import android.util.Log
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.zebra.rfid.api3.TagData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AccountCheckViewModel @Inject constructor(
    private val rfidHandler: ZebraRfidHandler
) : BaseViewModel(rfidHandler) {

    private val _acctCheckUiState = MutableStateFlow(AcctCheckUiState())
    val acctCheckUiState: StateFlow<AcctCheckUiState> = _acctCheckUiState.asStateFlow()

    private fun updateScannedStatus(isScanned: Boolean) {
        _acctCheckUiState.update { it.copy(isScanned = isScanned) }
    }

    override fun onReceivedTagId(id: String) {

    }

    override fun handleTagData(tagData: Array<TagData>) {
        updateScannedStatus(tagData.isNotEmpty())
        if (tagData.isNotEmpty()) stopScan()

        Log.d("TAG", "handleTagData: AccountCheck ${tagData.get(0).tagID}")
    }

    override fun handleTriggerPress(pressed: Boolean) {
        startScan()
    }

    override fun handleReaderConnected(isConnected: Boolean) {

    }

    data class AcctCheckUiState(
        val isScanned: Boolean = false
    )
}