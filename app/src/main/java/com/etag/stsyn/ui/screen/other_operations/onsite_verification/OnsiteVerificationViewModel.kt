package com.etag.stsyn.ui.screen.other_operations.onsite_verification

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import com.tzh.retrofit_module.domain.model.bookIn.safeCopy
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

    private val _getOnSiteVerifyItems =
        MutableStateFlow<ApiResponse<ItemWhereNotInResponse>>(ApiResponse.Default)
    val getOnSiteVerifyItems: StateFlow<ApiResponse<ItemWhereNotInResponse>> =
        _getOnSiteVerifyItems.asStateFlow()


    private val _onsiteVerificationUiState = MutableStateFlow(OnsiteVerificationUiState())
    val onsiteVerificationUiState: StateFlow<OnsiteVerificationUiState> =
        _onsiteVerificationUiState.asStateFlow()


    private val _totalScannedItems = MutableStateFlow<List<BoxItem?>>(emptyList())
    val totalScannedItems: StateFlow<List<BoxItem?>> = _totalScannedItems.asStateFlow()

    private val _outstandingItems = MutableStateFlow<List<BoxItem?>>(emptyList())
    val outstandingItems: StateFlow<List<BoxItem?>> = _outstandingItems.asStateFlow()

    private val _currentScannedItem = MutableStateFlow<BoxItem?>(null)
    val currentScannedItem: StateFlow<BoxItem?> = _currentScannedItem.asStateFlow()

    private val _scannedItemIndex = MutableStateFlow(-1)
    val scannedItemIndex: StateFlow<Int> = _scannedItemIndex.asStateFlow()

    private val _filterStatusMessage = MutableStateFlow<String?>(null)
    val filterStatusMessage: StateFlow<String?> = _filterStatusMessage

    override fun onReceivedTagId(id: String) {
        //  addScanItemToUiState("455341483030303030303036")
    }

    fun onReceivedTagIdTest() {
        val dummyEpc = listOf<String>(
            "020200000112",
            "020200000111",
            "4553413030303030303032",
            "020200000113",
            "020200000110",
            "455341483030303030303133",
            "020200001762",
            "020200004389",
            "020200002389",
            "020200004055",
            "020200004058",
            "020200004107",
            "020200002349",
            "020200002351",
            "020200004665",
            "020200004667",
            "020200004669",
            "76r5e45675645"
        )

        val dummyListTwo = listOf<String>(
            "020200000112",
            "020200000112"
        )
        addScannedItemAndMoveToTop(dummyEpc.random())


    }

    private fun addScannedItemAndMoveToTop(epc: String) {
        viewModelScope.launch {

            val allExistingItems = _onsiteVerificationUiState.value.allItemsFromApi
            val currentItems = allExistingItems.toMutableList()


            val foundIndex = currentItems.indexOfFirst { it.epc == epc }

            if (foundIndex != -1) {

                if (currentItems[foundIndex].isScanned) {
                    _filterStatusMessage.value = "Item Already found"
                } else {
                    _filterStatusMessage.value = null

                    val foundItem = currentItems.removeAt(foundIndex).safeCopy(isScanned = true)

                    // Move found item to the top, just below the last found item (if any)
                    currentItems.add(0, foundItem)
                    _currentScannedItem.value  = foundItem
                    _totalScannedItems.update {
                        it + foundItem
                    }
                    _onsiteVerificationUiState.update {
                        it.copy(allItemsFromApi = currentItems)
                    }

                    /*Optional for future*/
                    _onsiteVerificationUiState.update {
                        it.copy(itemsFromReader = it.itemsFromReader + foundItem)
                    }

                    addOutstandingItem()
                }

            }else{
                _filterStatusMessage.value = "Item not found in the list."
            }
        }
    }
    fun getOnSiteVerifyItems() {
        viewModelScope.launch {
            _getOnSiteVerifyItems.value = ApiResponse.Loading
            delay(1000)
            _getOnSiteVerifyItems.value = bookOutRepository.getOnSiteVerifyItem()

            shouldShowAuthorizationFailedDialog(_getOnSiteVerifyItems.value is ApiResponse.AuthorizationError)
            when (_getOnSiteVerifyItems.value) {
                is ApiResponse.Success -> {
                    val items = (_getOnSiteVerifyItems.value as ApiResponse.Success).data?.items
                        ?: emptyList()
                    _onsiteVerificationUiState.update {
                        it.copy(allItemsFromApi = items)
                    }
                    _outstandingItems.update {
                        items
                    }

                }


                is ApiResponse.ApiError -> {
                    _onsiteVerificationUiState.value =
                        OnsiteVerificationUiState(errorMessage = (_getOnSiteVerifyItems.value as ApiResponse.ApiError).message)
                }

                else -> {}
            }
        }
    }
 /*
 This Approch is to add scanned item to the list and update the UI by animatedIndex to list state
  private fun addScannedItem(id: String) {
        viewModelScope.launch {
            val allExistingItems = _onsiteVerificationUiState.value.allItemsFromApi
            val currentTotalScanItem = _totalScannedItems.value.toMutableList()
            val hasExist = id in currentTotalScanItem.map { it?.epc }

            try {
                val scannedItemResult = allExistingItems.find { it.epc == id}
                /**Index for use current scanned item's background color*/
                val index = allExistingItems.indexOfFirst { it.epc == id }
                if(!hasExist){
                    if(scannedItemResult !=null && index != -1){
                        currentTotalScanItem.add(scannedItemResult)
                        /**for use current scanned item's background color*/
                        _scannedItemIndex.value = index
                        _currentScannedItem.value = allExistingItems[index]
                        allExistingItems[index].isScanned =true

                        /*Optional for future*/
                        _onsiteVerificationUiState.update {
                            it.copy(itemsFromReader = it.itemsFromReader + scannedItemResult)
                        }

                        _totalScannedItems.update {
                            it + scannedItemResult
                        }
                        addOutstandingItem()
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
  */




    fun resetCurrentScannedItem() {
        _currentScannedItem.value = null
        _scannedItemIndex.value = -1
        removeAllScannedItems()
        removeAllOutstandingItems()
    }

    fun removeScannedBookInItem(currentItem: BoxItem) {
        viewModelScope.launch {

            val currentList = _totalScannedItems.value
            val indexToRemove = currentList.indexOf(currentItem)
            val updatedList = currentList.toMutableList().apply {
                removeAt(indexToRemove)
            }
            _totalScannedItems.value = updatedList

            // update outstanding when scanned items change
            addOutstandingItem()
        }
    }

    private fun removeAllScannedItems() {
        _totalScannedItems.value = emptyList()
        addOutstandingItem()
    }

    fun resetAllScannedStatus(){
        viewModelScope.launch {
            val updatedItems = _onsiteVerificationUiState.value.allItemsFromApi.map { it.safeCopy(isScanned = false) }
            _onsiteVerificationUiState.update {
                it.copy(allItemsFromApi = updatedItems)
            }
            _filterStatusMessage.value = "All items have been reset."
        }
    }

    private fun removeAllOutstandingItems() {
        _outstandingItems.value = emptyList()
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
                    val allExistingItems = _onsiteVerificationUiState.value.allItemsFromApi.toMutableList()
                    _totalScannedItems.collect { items ->
                        items.forEach { bookInItem ->
                            allExistingItems.remove(bookInItem)
                        }
                        _outstandingItems.update { allExistingItems }
                    }
                }
            }
        }
    }




    init {
        getOnSiteVerifyItems()
        addOutstandingItem()
    }


}


data class OnsiteVerificationUiState(
    val allItemsFromApi: List<BoxItem> = emptyList(),
    val errorMessage: String = "",
    val itemsFromReader: List<BoxItem> = emptyList()
)
