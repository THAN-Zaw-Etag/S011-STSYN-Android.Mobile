package com.etag.stsyn.ui.screen.book_in.book_in

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.etag.stsyn.data.localStorage.LocalDataStore
import com.tzh.retrofit_module.data.repository.BookInRepository
import com.tzh.retrofit_module.domain.model.bookIn.BookInResponse
import com.tzh.retrofit_module.util.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookInViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler,
    private val bookInRepository: BookInRepository,
    private val localDataStore: LocalDataStore
) : BaseViewModel(rfidHandler) {

    private val _bookInItems = MutableStateFlow<ApiResponse<BookInResponse>>(ApiResponse.Default)
    val bookInItems: StateFlow<ApiResponse<BookInResponse>> = _bookInItems.asStateFlow()

    init {
        viewModelScope.launch {
            localDataStore.getUser.collect {
                Log.d("TAG", "book in token: ${it.token}")
                getBookInItems("CS", "2", it.id, it.token)
            }
        }
    }

    override fun onReceivedTagId(id: String) {
        // handled scanned tags here
    }

    fun getBookInItems(
        store: String,
        csNo: String,
        userId: String,
        token: String
    ) {
        viewModelScope.launch {
            _bookInItems.value = ApiResponse.Loading
            _bookInItems.value = bookInRepository.getBookInItems(store, csNo, userId, token)
            rfidUiState.value.scannedItems
        }
    }

}