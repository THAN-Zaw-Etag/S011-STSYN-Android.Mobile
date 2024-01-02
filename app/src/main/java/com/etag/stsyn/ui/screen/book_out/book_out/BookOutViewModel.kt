package com.etag.stsyn.ui.screen.book_out.book_out

import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.zebra.rfid.api3.TagData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BookOutViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler
) : BaseViewModel(rfidHandler) {

    private val _bookOutUiState = MutableStateFlow(BookOutUiState())
    val bookOutUiState: StateFlow<BookOutUiState> = _bookOutUiState

    fun updateScannedItems(items: List<String>) {
        _bookOutUiState.update {
            val updatedItems = it.scannedItems.toMutableList().apply {
                addAll(items)
            }
            it.copy(scannedItems = updatedItems.toMutableList())
        }
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

    data class BookOutUiState(
        val scannedItems: MutableList<String> = mutableListOf<String>()
    )
}