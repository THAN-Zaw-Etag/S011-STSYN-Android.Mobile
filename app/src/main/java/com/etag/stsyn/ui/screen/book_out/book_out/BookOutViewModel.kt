package com.etag.stsyn.ui.screen.book_out.book_out

import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class BookOutViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler
) : BaseViewModel(rfidHandler) {

    val TAG = "BookOut ViewModel"

    private val _bookOutUiState = MutableStateFlow(BookOutUiState())
    val bookOutUiState: StateFlow<BookOutUiState> = _bookOutUiState.asStateFlow()

    /*private val _items = MutableStateFlow(mutableListOf<String>())
    val items: StateFlow<List<String>> = _items.asStateFlow()

    private fun addItem(item: String) {
        val currentItems = _items.value.toMutableList()

        isScanning

        val hasExisted = item in currentItems
        if (!hasExisted) {
            currentItems.add(item)
            _items.value = currentItems // Update the StateFlow with the new list
        }
    }

    fun removeItem(item: String) {
        val currentItems = _items.value.toMutableList()
        currentItems.remove(item)
        _items.value = currentItems
    }*/


    override fun onReceivedTagId(id: String) {
    }

    data class BookOutUiState(
        val scannedItems: MutableList<String> = mutableListOf()
    )
}