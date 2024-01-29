package com.etag.stsyn.ui.screen.book_in.book_in

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.model.book_in.SaveBookInRequest
import com.tzh.retrofit_module.data.settings.AppConfiguration
import com.tzh.retrofit_module.domain.model.bookIn.BookInItem
import com.tzh.retrofit_module.domain.model.bookIn.BookInResponse
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.repository.BookInRepository
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
    private val localDataStore: LocalDataStore,
    private val appConfig: AppConfiguration
) : BaseViewModel(rfidHandler) {

    private val _bookInItems = MutableStateFlow<ApiResponse<BookInResponse>>(ApiResponse.Default)
    val bookInItemsResponse: StateFlow<ApiResponse<BookInResponse>> = _bookInItems.asStateFlow()

    private val _scannedBookInItems = MutableStateFlow<List<BookInItem?>>(emptyList())
    val scannedBookInItems: StateFlow<List<BookInItem?>> = _scannedBookInItems.asStateFlow()

    private val _savedBookInResponse =
        MutableStateFlow<ApiResponse<NormalResponse>>(ApiResponse.Default)
    val savedBookInResponse: StateFlow<ApiResponse<NormalResponse>> =
        _savedBookInResponse.asStateFlow()

    private val _outstandingItems = MutableStateFlow<List<BookInItem?>>(emptyList())
    val outstandingItems: StateFlow<List<BookInItem?>> = _outstandingItems.asStateFlow()

    val user = localDataStore.getUser
    val appConfiguration = appConfig.appConfig

    init {
        viewModelScope.launch {
            localDataStore.getUser.collect {
                getBookInItems(userId = it.userId)
            }
        }
        addOutstandingItem()
    }

    override fun onReceivedTagId(id: String) {
        Log.d("TAG", "onReceivedTagId: $id")
        // handled scanned tags here
        addScannedItem(id)
    }

    private fun addOutstandingItem() {
        viewModelScope.launch {
            rfidUiState.collect {
                if (!it.isScanning) {
                    if (_bookInItems.value is ApiResponse.Success) {
                        val bookInItems =
                            (_bookInItems.value as ApiResponse.Success<BookInResponse>).data!!.items.toMutableList()

                        _scannedBookInItems.collect { items ->
                            items.forEach { bookInItem ->
                                bookInItems.remove(bookInItem)
                            }
                            _outstandingItems.update { bookInItems }
                        }
                    }
                }
            }
        }
    }

    private fun addScannedItem(id: String) {
        viewModelScope.launch {
            if (_bookInItems.value is ApiResponse.Success) {
                val bookInItems =
                    (_bookInItems.value as ApiResponse.Success<BookInResponse>).data!!.items

                val currentItems = _scannedBookInItems.value.toMutableList()
                val hasExisted = id in currentItems.map { it?.epc }

                try {
                    val scannedItem = bookInItems.find { it.epc == id }
                    if (!hasExisted) {
                        if (scannedItem != null) {
                            currentItems.add(scannedItem)
                            _scannedBookInItems.update {
                                it + scannedItem
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

            // update outstanding when scanned items change
            addOutstandingItem()
        }
    }

    fun removeAllScannedItems() {
        _scannedBookInItems.value = emptyList()

        // update outstanding when scanned items change
        addOutstandingItem()
    }

    fun saveBookIn(saveBookInRequest: SaveBookInRequest) {
        viewModelScope.launch {
            _savedBookInResponse.value = ApiResponse.Loading
            delay(1000)
            _savedBookInResponse.value =
                bookInRepository.saveBookIn(saveBookInRequest = saveBookInRequest)
        }
    }

    fun getBoxItemsForBookIn() {
        viewModelScope.launch {

        }
    }

    private fun getBookInItems(
        userId: String
    ) {
        viewModelScope.launch {
            _bookInItems.value = ApiResponse.Loading
            delay(1000)
            appConfiguration.collect {
                _bookInItems.value = bookInRepository.getBookInItems(
                    store = it.store.name,
                    csNo = it.csNo,
                    userId = userId
                )
                updateAuthorizationFailedDialogVisibility(_bookInItems.value is ApiResponse.AuthorizationError)

                when (_bookInItems.value) {
                    is ApiResponse.ApiError -> {
                        updateErrorMessage((_bookInItems.value as ApiResponse.ApiError).message)
                    }

                    else -> {}
                }
            }
        }
    }
}