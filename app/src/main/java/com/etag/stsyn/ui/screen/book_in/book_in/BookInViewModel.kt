package com.etag.stsyn.ui.screen.book_in.book_in

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.etag.stsyn.data.localStorage.LocalDataStore
import com.etag.stsyn.util.toToken
import com.tzh.retrofit_module.data.model.book_in.SaveBookInRequest
import com.tzh.retrofit_module.data.repository.BookInRepository
import com.tzh.retrofit_module.domain.model.bookIn.BookInItem
import com.tzh.retrofit_module.domain.model.bookIn.BookInResponse
import com.tzh.retrofit_module.util.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    private val _scannedBookInItems = MutableStateFlow<List<BookInItem>>(emptyList())
    val scannedBookInItems: StateFlow<List<BookInItem>> = _scannedBookInItems.asStateFlow()

    init {
        viewModelScope.launch {
            localDataStore.getUser.collect {
                getBookInItems(store = "CS", csNo = "2", userId = it.id, token = it.token)
            }
            getScannedItemsDetails()
        }
    }

    val user = localDataStore.getUser

    override fun onReceivedTagId(id: String) {
        // handled scanned tags here
        Log.d("TAG", "onReceivedTagId: $id")
    }

    //private fun addScannedItem()

    private fun getScannedItemsDetails() {
        viewModelScope.launch {
            val itemDetails =
                (_bookInItems.value as ApiResponse.Success<BookInResponse>).data?.items
                    ?: emptyList()
            rfidUiState.collect {
                it.scannedItems.forEachIndexed { index, s ->
                    println("scannedItem: $s")
                    if (s in itemDetails.map { it.epc }) {
                        val currentItems = _scannedBookInItems.value.toMutableList()
                        currentItems.add(itemDetails.get(index))
                        _scannedBookInItems.value = currentItems
                    }
                }
            }
        }
    }

    fun saveBookIn(saveBookInRequest: SaveBookInRequest) {
        viewModelScope.launch {
            user.collect {
                bookInRepository.saveBookIn(
                    token = it.token.toToken(),
                    saveBookInRequest = saveBookInRequest
                )
            }
        }
    }

    private fun getBookInItems(
        store: String,
        csNo: String,
        userId: String,
        token: String
    ) {
        viewModelScope.launch {
            _bookInItems.value = ApiResponse.Loading
            delay(1000)
            _bookInItems.value = bookInRepository.getBookInItems(
                store = store,
                csNo = csNo,
                userId = userId,
                token = token
            )
            rfidUiState.value.scannedItems
        }
    }
}