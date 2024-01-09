package com.etag.stsyn.ui.screen.acct_check

import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
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

    init {
        updateScanType(ScanType.Single)
    }

    private fun updateScannedStatus(isScanned: Boolean) {
        _acctCheckUiState.update { it.copy(isScanned = isScanned) }
    }

    override fun onReceivedTagId(id: String) {

    }

    data class AcctCheckUiState(
        val isScanned: Boolean = false
    )
}