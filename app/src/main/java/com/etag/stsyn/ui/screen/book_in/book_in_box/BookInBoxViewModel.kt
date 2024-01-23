package com.etag.stsyn.ui.screen.book_in.book_in_box

import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import com.tzh.retrofit_module.domain.model.bookIn.GetAllBookInItemsOfBoxResponse
import com.tzh.retrofit_module.domain.model.bookIn.SelectBoxForBookInResponse
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
class BookInBoxViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler,
    private val localDataStore: LocalDataStore,
    private val bookInRepository: BookInRepository,
) : BaseViewModel(rfidHandler) {

    private val _getBoxItemsForBookInResponse =
        MutableStateFlow<ApiResponse<SelectBoxForBookInResponse>>(ApiResponse.Default)
    val boxItemsForBookInResponse: StateFlow<ApiResponse<SelectBoxForBookInResponse>> =
        _getBoxItemsForBookInResponse.asStateFlow()

    private val _getAllItemsOfBox =
        MutableStateFlow<ApiResponse<GetAllBookInItemsOfBoxResponse>>(ApiResponse.Default)
    val getAllItemsOfBox: StateFlow<ApiResponse<GetAllBookInItemsOfBoxResponse>> =
        _getAllItemsOfBox.asStateFlow()

    private val _bookInBoxUiState = MutableStateFlow(BookInBoxUiState())
    val bookInBoxUiState: StateFlow<BookInBoxUiState> = _bookInBoxUiState.asStateFlow()

    private var boxItems = MutableStateFlow<List<BoxItem>>(emptyList())

    val scannedItemsList = MutableStateFlow<List<String>>(emptyList())

    private val user = localDataStore.getUser

    init {
        updateScanType(ScanType.Single)
        getAllBoxesForBookInItem()
    }

    private fun addScannedItemToList(id: String) {
        scannedItemsList.update { currentList ->
            if (id !in currentList) {
                currentList + id
            } else {
                currentList
            }
        }
    }

    override fun onReceivedTagId(id: String) {
        if (_getBoxItemsForBookInResponse.value is ApiResponse.Success) {
            boxItems.value =
                (_getBoxItemsForBookInResponse.value as ApiResponse.Success<SelectBoxForBookInResponse>).data!!.items
            val scannedBoxItem = boxItems.value.find { it.epc == id }
            if (scannedBoxItem != null) {
                _bookInBoxUiState.update { it.copy(scannedBox = scannedBoxItem) }
                getAllBookInItemsOfBox(
                    box = scannedBoxItem.box,
                    status = scannedBoxItem.itemStatus
                )
            }

            if (boxItems.value.isNotEmpty()) updateScanType(ScanType.Multi) else updateScanType(
                ScanType.Single
            )

            val hasCurrentItemScanned = id in boxItems.value.map { it.epc }
            if (hasCurrentItemScanned) addScannedItemToList(id)
        }
    }

    fun toggleVisualCheck(enable: Boolean) {
        if (enable) {
            scannedItemsList.update { boxItems.value.map { it.id } }
        } else scannedItemsList.update { emptyList() }
    }

    private fun getAllBoxesForBookInItem() {
        viewModelScope.launch {
            _getBoxItemsForBookInResponse.value = ApiResponse.Loading
            delay(1000)
            user.collect {
                _getBoxItemsForBookInResponse.value =
                    bookInRepository.getBoxItemsForBookIn(it.userId)
            }
        }
    }

    fun getAllBookInItemsOfBox(
        box: String,
        status: String,
    ) {
        viewModelScope.launch {
            user.collect {
                _getAllItemsOfBox.value = ApiResponse.Loading
                _getAllItemsOfBox.value = bookInRepository.getAllBookItemsOfBox(
                    box = box,
                    status = status,
                    loginUserId = it.userId
                )

                if (_getAllItemsOfBox.value is ApiResponse.Success) {
                    val allItems = (_getAllItemsOfBox.value as ApiResponse.Success).data!!.items
                    viewModelScope.launch {
                        _getAllItemsOfBox.collect { response ->
                            _bookInBoxUiState.update { it.copy(allItemsOfBox = allItems.toMutableList()) }
                        }
                    }
                }
            }
        }
    }

    data class BookInBoxUiState(
        val scannedBox: BoxItem = BoxItem(),
        val allItemsOfBox: MutableList<BoxItem> = mutableListOf()
    )
}