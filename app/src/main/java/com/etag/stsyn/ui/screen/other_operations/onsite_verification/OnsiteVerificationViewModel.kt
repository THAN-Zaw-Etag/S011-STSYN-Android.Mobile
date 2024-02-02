package com.etag.stsyn.ui.screen.other_operations.onsite_verification

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.settings.AppConfiguration
import com.tzh.retrofit_module.domain.model.bookIn.BookInItem
import com.tzh.retrofit_module.domain.model.bookIn.BookInResponse
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import com.tzh.retrofit_module.domain.model.bookOut.ItemWhereNotInResponse
import com.tzh.retrofit_module.domain.repository.BookOutRepository
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
class OnsiteVerificationViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler,
    private val bookOutRepository: BookOutRepository,
) : BaseViewModel(rfidHandler) {
    val TAG = "OnsiteVerificationViewModel"

    private val _getItemsWhereNotIn =
        MutableStateFlow<ApiResponse<ItemWhereNotInResponse>>(ApiResponse.Default)
    val getItemsWhereNotIn: StateFlow<ApiResponse<ItemWhereNotInResponse>> =
        _getItemsWhereNotIn.asStateFlow()


    private val _onsiteVerificationUiState = MutableStateFlow(OnsiteVerificationUiState())
    val onsiteVerificationUiState: StateFlow<OnsiteVerificationUiState> =
        _onsiteVerificationUiState.asStateFlow()


    private val _scannedItems = MutableStateFlow<List<BoxItem?>>(emptyList())
    val scannedItems: StateFlow<List<BoxItem?>> = _scannedItems.asStateFlow()

    private val _outstandingItems = MutableStateFlow<List<BoxItem?>>(emptyList())
    val outstandingItems: StateFlow<List<BoxItem?>> = _outstandingItems.asStateFlow()

    private val _currentScannedItem = MutableStateFlow<BoxItem?>(null)
    val currentScannedItem: StateFlow<BoxItem?> = _currentScannedItem.asStateFlow()


    override fun onReceivedTagId(id: String) {
        addScanItemToUiState("455341483030303030303036")
    }

    fun test() {
        val dummyEpc = listOf<String>(
            "020200000112",
            "020200000111",
            "4553413030303030303032",
            "020200000113",
            "020200000110",
            "455341483030303030303133"
        )
        updateScannedStatus(dummyEpc.random())

        addScannedItem(dummyEpc.random())


    }

    fun getItemsWhereNotIn() {
        viewModelScope.launch {
            _getItemsWhereNotIn.value = ApiResponse.Loading
            delay(1000)
            _getItemsWhereNotIn.value = bookOutRepository.getAllNotInItems()

            shouldShowAuthorizationFailedDialog(_getItemsWhereNotIn.value is ApiResponse.AuthorizationError)
            when (_getItemsWhereNotIn.value) {

                is ApiResponse.Success -> {
                    val items = (_getItemsWhereNotIn.value as ApiResponse.Success).data?.items
                        ?: emptyList()
                    _onsiteVerificationUiState.update {
                        it.copy(allItemsFromApi = items)
                    }

                }


                is ApiResponse.ApiError -> {
                    _onsiteVerificationUiState.value =
                        OnsiteVerificationUiState(errorMessage = (_getItemsWhereNotIn.value as ApiResponse.ApiError).message)
                }

                else -> {}
            }
        }
    }

    private fun addScannedItem(id: String) {
        viewModelScope.launch {
            if (_getItemsWhereNotIn.value is ApiResponse.Success) {
                val bookInItems =
                    (_getItemsWhereNotIn.value as ApiResponse.Success).data!!.items

                val currentItems = _scannedItems.value.toMutableList()
                val hasExisted = id in currentItems.map { it?.epc }

                try {
                    val scannedItem = bookInItems.find { it.epc == id }
                    if (!hasExisted) {
                        if (scannedItem != null) {
                            currentItems.add(scannedItem)
                            _currentScannedItem.value = scannedItem
                            _scannedItems.update {
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

    fun removeScannedBookInItem(currentItem: BoxItem) {
        viewModelScope.launch {

            val currentList = _scannedItems.value
            val indexToRemove = currentList.indexOf(currentItem)
            val updatedList = currentList.toMutableList().apply {
                removeAt(indexToRemove)
            }
            _scannedItems.value = updatedList

            // update outstanding when scanned items change
            addOutstandingItem()
        }
    }

    fun removeAllScannedItems() {
        _scannedItems.value = emptyList()
        addOutstandingItem()
    }

    private fun addScanItemToUiState(id: String) {
        val hasExist = id in onsiteVerificationUiState.value.itemsFromReader.map { it.epc }
        if (hasExist) {
            return
        }
        val item = onsiteVerificationUiState.value.allItemsFromApi.find { it.epc == id }
        if (item != null) {
            _onsiteVerificationUiState.update {
                it.copy(itemsFromReader = it.itemsFromReader + item)
            }
        }
    }

    fun updateScannedStatus(epc: String) {
        val items = onsiteVerificationUiState.value.allItemsFromApi.toMutableList()
        val index = items.indexOfFirst { it.epc == epc }
        if (index != -1) {
            items[index].isScanned = true
        }
        Log.d(TAG, "updateScannedStatus: $index")
    }

    fun removeItemFromReader(index: Int) {
        val items = onsiteVerificationUiState.value.itemsFromReader.toMutableList()
        items.removeAt(index)
        _onsiteVerificationUiState.update {
            it.copy(itemsFromReader = items)
        }
    }

    private fun addOutstandingItem() {
        viewModelScope.launch {
            rfidUiState.collect {
                if (!it.isScanning) {
                    if (_getItemsWhereNotIn.value is ApiResponse.Success) {
                        val bookInItems =
                            (_getItemsWhereNotIn.value as ApiResponse.Success).data!!.items.toMutableList()

                        _scannedItems.collect { items ->
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

    init {
        getItemsWhereNotIn()
        addOutstandingItem()
    }


}


data class OnsiteVerificationUiState(
    val allItemsFromApi: List<BoxItem> = emptyList(),
    val errorMessage: String = "",
    val itemsFromReader: List<BoxItem> = emptyList()
)
