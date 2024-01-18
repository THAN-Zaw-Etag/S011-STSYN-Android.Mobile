package com.etag.stsyn.ui.screen.book_in.book_in

import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.tzh.retrofit_module.data.localStorage.LocalDataStore
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookInViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler,
    private val bookInRepository: BookInRepository,
    private val localDataStore: LocalDataStore
) : BaseViewModel(rfidHandler) {

    private val _bookInItems = MutableStateFlow<ApiResponse<BookInResponse>>(ApiResponse.Default)
    val bookInItemsResponse: StateFlow<ApiResponse<BookInResponse>> = _bookInItems.asStateFlow()

    private val _scannedBookInItems = MutableStateFlow<List<BookInItem?>>(emptyList())
    val scannedBookInItems: StateFlow<List<BookInItem?>> = _scannedBookInItems.asStateFlow()

    //private val _boxItems

    val user = localDataStore.getUser

    init {
        viewModelScope.launch {
            localDataStore.getUser.collect {
                getBookInItems(store = "CS", csNo = "2", userId = it.userId)
            }
        }
    }

    override fun onReceivedTagId(id: String) {
        // handled scanned tags here
        addScannedItem(id)
    }

    private fun addScannedItem(id: String) {
        viewModelScope.launch {
            if (_bookInItems.value is ApiResponse.Success) {
                val bookInItems =
                    (_bookInItems.value as ApiResponse.Success<BookInResponse>).data!!.items
                val currentItems = _scannedBookInItems.value.toMutableList()
                val hasExisted = id in currentItems.map { it?.epc }

                try {
                    if (!hasExisted) {
                        val item = bookInItems.find { it.epc == id }
                        if (item != null) {
                            currentItems.add(item)
                            _scannedBookInItems.update {
                                it + item
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun removeScannedBookInItem(currentItem: BookInItem) {
        viewModelScope.launch {
            val currentList = _scannedBookInItems.value
            val indexToRemove = currentList.indexOf(currentItem)
            val updatedList = currentList.toMutableList().apply {
                removeAt(indexToRemove)
            }
            _scannedBookInItems.value = updatedList
        }
    }

    fun removeAllScannedItems() {
        _scannedBookInItems.value = emptyList()
    }

    fun saveBookIn(saveBookInRequest: SaveBookInRequest) {
        viewModelScope.launch {
            user.collect {
                bookInRepository.saveBookIn(
                    saveBookInRequest = saveBookInRequest
                )
            }
        }
    }

    fun getBoxItemsForBookIn() {
        viewModelScope.launch {

        }
    }

    private fun getBookInItems(
        store: String,
        csNo: String,
        userId: String
    ) {
        viewModelScope.launch {
            _bookInItems.value = ApiResponse.Loading
            delay(1000)
            _bookInItems.value = bookInRepository.getBookInItems(
                store = store,
                csNo = csNo,
                userId = userId
            )
        }
    }
}