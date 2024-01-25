package com.etag.stsyn.ui.screen.book_in.book_in_box

import android.util.Log
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

    private val _boxItemsForBookInResponse =
        MutableStateFlow<ApiResponse<SelectBoxForBookInResponse>>(ApiResponse.Default)
    val boxItemsForBookInResponse: StateFlow<ApiResponse<SelectBoxForBookInResponse>> =
        _boxItemsForBookInResponse.asStateFlow()

    private val _getAllItemsOfBox =
        MutableStateFlow<ApiResponse<GetAllBookInItemsOfBoxResponse>>(ApiResponse.Default)
    val getAllItemsOfBox: StateFlow<ApiResponse<GetAllBookInItemsOfBoxResponse>> =
        _getAllItemsOfBox.asStateFlow()

    private val _bookInBoxUiState = MutableStateFlow(BookInBoxUiState())
    val bookInBoxUiState: StateFlow<BookInBoxUiState> = _bookInBoxUiState.asStateFlow()

    private var boxItems = MutableStateFlow<List<BoxItem>>(emptyList())  // items from api
    val scannedItemsList = MutableStateFlow<List<String>>(emptyList()) // scanned tag list

    private val user = localDataStore.getUser

    init {
        updateScanType(ScanType.Single)
        getAllBoxesForBookInItem()
    }

    private fun addScannedItemToList(id: String) {
        Log.d("TAG", "addScannedItemToList: $id")
        scannedItemsList.update { currentList ->
            if (id in currentList) {
                currentList
            } else {
                currentList + id
            }
        }
    }

    private fun checkUsCaseByBoxName(boxName: String) {
        viewModelScope.launch {
            val response = bookInRepository.checkUSCaseByBox(boxName)
            when (response) {
                is ApiResponse.Success -> {
                    _bookInBoxUiState.update {
                        it.copy(
                            isUsCase = response.data?.isUsCase ?: false
                        )
                    }
                }

                is ApiResponse.AuthorizationError -> updateAuthorizationFailedDialogVisibility(true)
                else -> {}
            }
        }
    }

    override fun onReceivedTagId(id: String) {

        // stop scan when all items are scanned
        if (scannedItemsList.value.size == _bookInBoxUiState.value.allItemsOfBox.size) stopScan()

        if (_boxItemsForBookInResponse.value is ApiResponse.Success) {
            boxItems.value =
                (_boxItemsForBookInResponse.value as ApiResponse.Success<SelectBoxForBookInResponse>).data!!.items
            val scannedBoxItem = boxItems.value.find { it.epc == id }
            if (scannedBoxItem != null) {
                _bookInBoxUiState.update { it.copy(scannedBox = scannedBoxItem) }
                getAllBookInItemsOfBox(
                    box = scannedBoxItem.box,
                    status = scannedBoxItem.itemStatus
                )

                // check box is us case
                checkUsCaseByBoxName(scannedBoxItem.box)
            }

            when (_getAllItemsOfBox.value) {
                is ApiResponse.Success -> {
                    val boxesOfItem = _bookInBoxUiState.value.allItemsOfBox ?: emptyList()
                    if (boxesOfItem.isNotEmpty()) updateScanType(ScanType.Multi) else updateScanType(
                        ScanType.Single
                    )

                    val hasCurrentItemScanned = id in boxesOfItem.map { it.epc }
                    if (hasCurrentItemScanned) addScannedItemToList(id)
                }

                else -> {}
            }
        }
    }

    fun refreshScannedBox() {
        viewModelScope.launch {
            boxItems.update { emptyList() }
            scannedItemsList.update { emptyList() }
            _bookInBoxUiState.update {
                it.copy(scannedBox = BoxItem(), allItemsOfBox = mutableListOf())
            }
        }
    }

    fun toggleVisualCheck(enable: Boolean) {
        _bookInBoxUiState.update { it.copy(isChecked = enable) }
        if (enable) {
            scannedItemsList.update { _bookInBoxUiState.value.allItemsOfBox.map { it.epc } }
        } else scannedItemsList.update { emptyList() }
    }

    fun resetScannedItems() {
        scannedItemsList.update { emptyList() }
    }

    private fun getAllBoxesForBookInItem() {
        viewModelScope.launch {
            _boxItemsForBookInResponse.value = ApiResponse.Loading
            delay(1000)
            user.collect {
                _boxItemsForBookInResponse.value = bookInRepository.getBoxItemsForBookIn(it.userId)

                when (_boxItemsForBookInResponse.value) {
                    is ApiResponse.Success -> {
                        val boxes =
                            (_boxItemsForBookInResponse.value as ApiResponse.Success<SelectBoxForBookInResponse>).data?.items
                                ?: emptyList()
                        _bookInBoxUiState.update { it.copy(allBoxes = boxes) }
                    }

                    is ApiResponse.AuthorizationError -> updateAuthorizationFailedDialogVisibility(
                        true
                    )

                    else -> {}
                }
            }
        }
    }

    private fun getAllBookInItemsOfBox(
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

                when (_getAllItemsOfBox.value) {
                    is ApiResponse.Success -> {
                        val allItems = (_getAllItemsOfBox.value as ApiResponse.Success).data!!.items
                        viewModelScope.launch {
                            _getAllItemsOfBox.collect { response ->
                                _bookInBoxUiState.update { it.copy(allItemsOfBox = allItems.toMutableList()) }
                            }
                        }
                    }

                    is ApiResponse.AuthorizationError -> {
                        updateAuthorizationFailedDialogVisibility(true)
                    }

                    else -> {}
                }
            }
        }
    }

    data class BookInBoxUiState(
        val scannedBox: BoxItem = BoxItem(),
        val isUsCase: Boolean = false,
        val isChecked: Boolean = false,
        val allBoxes: List<BoxItem> = listOf(),
        val allItemsOfBox: MutableList<BoxItem> = mutableListOf()
    )
}