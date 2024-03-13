package com.etag.stsyn.ui.screen.other_operations.onsite_verification


import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.ClickEvent
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.etag.stsyn.util.ErrorMessages
import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.model.onsiteverification.SaveOnSiteVerificationRq
import com.tzh.retrofit_module.data.model.onsiteverification.StockTake
import com.tzh.retrofit_module.data.settings.AppConfiguration
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import com.tzh.retrofit_module.domain.model.bookIn.safeCopy
import com.tzh.retrofit_module.domain.model.bookOut.OnSiteVerificationResponse
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.repository.BookOutRepository
import com.tzh.retrofit_module.util.ApiResponse
import com.tzh.retrofit_module.util.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnsiteVerificationViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler,
    private val bookOutRepository: BookOutRepository,
    appConfiguration: AppConfiguration,
    private val localDataStore: LocalDataStore,
) : BaseViewModel(rfidHandler) {
    private val _getOnSiteVerifyItems =
        MutableStateFlow<ApiResponse<OnSiteVerificationResponse>>(ApiResponse.Default)
    val getOnSiteVerifyItems: StateFlow<ApiResponse<OnSiteVerificationResponse>> =
        _getOnSiteVerifyItems.asStateFlow()

    private val _saveOnSiteVerification =
        MutableStateFlow<ApiResponse<NormalResponse>>(ApiResponse.Default)
    val saveOnSiteVerification: StateFlow<ApiResponse<NormalResponse>> =_saveOnSiteVerification.asStateFlow()


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

    private val settingsFlow = appConfiguration.appConfig

    private val _hasScanned = MutableStateFlow(false)
    val hasScanned: StateFlow<Boolean> = _hasScanned.asStateFlow()
    override fun onReceivedTagId(id: String) {
        addScannedItemAndMoveToTop(id)
        _hasScanned.value = true
    }

    fun onReceivedTagIdTest() {
        val dummyEpc = listOf(
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

        _hasScanned.value = true
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
                    _currentScannedItem.value = foundItem
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

            } else {
                _filterStatusMessage.value = "${ErrorMessages.ITEM_CANNOT_BE_FOUND_IN_THE_LIST} \n $epc"
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
                    updateClickEvent(ClickEvent.Default)

                }


                is ApiResponse.ApiError -> {
                    _onsiteVerificationUiState.value =
                        OnsiteVerificationUiState(errorMessage = (_getOnSiteVerifyItems.value as ApiResponse.ApiError).message)
                    updateClickEvent(ClickEvent.Default)
                }

                else -> {
                    updateClickEvent(ClickEvent.Default)
                }
            }
        }
    }

    fun saveOnSiteVerification() {
        val date = DateUtil.getCurrentDateTimeFormattedWithZone()
        viewModelScope.launch {
            val settings = settingsFlow.first()
            val loginUserId = localDataStore.getUser.first().userId
            val stokeList = arrayListOf<StockTake>()

            val allDoneItems = _totalScannedItems.value
            val allOutstandingItems = _outstandingItems.value

            if (allDoneItems.isNotEmpty()) {
                allDoneItems.forEach { boxItem ->
                    val stockTake = StockTake(
                        chkStatusId = 1,
                        date = date,
                        handheldReaderId = settings.handheldReaderId.toInt(),
                        isDone = true,
                        isStockTake = false,
                        itemId = boxItem?.id?.toInt() ?: 0,
                        shift = "ONSITE",
                        userId = loginUserId.toInt()
                    )
                    stokeList.add(stockTake)
                }
            }

            if (allOutstandingItems.isNotEmpty()) {
                allOutstandingItems.forEach { boxItem ->
                    val stockTake = StockTake(
                        chkStatusId = 1,
                        date = date,
                        handheldReaderId = settings.handheldReaderId.toInt(),
                        isDone = false,
                        isStockTake = false,
                        itemId = boxItem?.id?.toInt() ?: 0,
                        shift = "ONSITE",
                        userId = loginUserId.toInt()
                    )
                    stokeList.add(stockTake)
                }
            }
            val saveOnSiteVerificationRq = SaveOnSiteVerificationRq(
                stockTakes = stokeList
            )



            _saveOnSiteVerification.value = ApiResponse.Loading
            delay(1000)
            _saveOnSiteVerification.value =
                bookOutRepository.saveOnSiteVerification(saveOnSiteVerificationRq)
            shouldShowAuthorizationFailedDialog(_saveOnSiteVerification.value is ApiResponse.AuthorizationError)
            when (_saveOnSiteVerification.value) {
                is ApiResponse.Success -> {
                    _filterStatusMessage.value = "Items saved successfully"

                }

                is ApiResponse.ApiError -> {
                    _filterStatusMessage.value =
                        (_saveOnSiteVerification.value as ApiResponse.ApiError).message
                }

                else -> {}
            }
        }
    }

    fun removeScannedBookInItemByIndex(index: Int) {
        viewModelScope.launch {
            val currentList = _onsiteVerificationUiState.value.allItemsFromApi
            if (index >= 0 && index < currentList.size) {
                val removedItemEpc = currentList[index].epc
                val updatedList = currentList.toMutableList().apply {
                    removeAt(index)
                }
                _onsiteVerificationUiState.update {
                    it.copy(allItemsFromApi = updatedList)
                }

                val updatedScannedItems =
                    _totalScannedItems.value.filterNot { it?.epc == removedItemEpc }
                _totalScannedItems.value = updatedScannedItems

                val updateOutstandingItems =
                    _outstandingItems.value.filterNot { it?.epc == removedItemEpc }
                _outstandingItems.value = updateOutstandingItems
            }
        }
    }

    private fun removeAllScannedItems() {
        _totalScannedItems.value = emptyList()
        addOutstandingItem()
    }

    fun resetAll(isShowToast:Boolean = false) {
        viewModelScope.launch {
            val updatedItems =
                _onsiteVerificationUiState.value.allItemsFromApi.map { it.safeCopy(isScanned = false) }
            _onsiteVerificationUiState.update {
                it.copy(allItemsFromApi = updatedItems)
            }

            if (isShowToast){
                _filterStatusMessage.value = "All items have been reset."
            }else{
                _filterStatusMessage.value = null
            }
            _currentScannedItem.value = null
            _scannedItemIndex.value = -1
            removeAllScannedItems()
            removeAllOutstandingItems()
        }
    }


    private fun removeAllOutstandingItems() {
        _outstandingItems.value = emptyList()
    }

    private fun addOutstandingItem() {
        viewModelScope.launch {
            rfidUiState.collect {
                if (!it.isScanning) {
                    val allExistingItems =
                        _onsiteVerificationUiState.value.allItemsFromApi.toMutableList()
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

    private fun resetSaveOnSiteVerification() {
        _saveOnSiteVerification.value = ApiResponse.Default
    }

    init {
        getOnSiteVerifyItems()
        addOutstandingItem()
        handleClickEvent()
    }

    private fun handleClickEvent() {
        viewModelScope.launch {
            clickEventFlow.collect {
                when (it) {
                    is ClickEvent.ClickAfterSave -> {
                        doTasksAfterSave()
                        resetAll(isShowToast = false)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun doTasksAfterSave() {
        _onsiteVerificationUiState.update { it.copy(allItemsFromApi = emptyList()) }
        updateSuccessDialogVisibility(false)
        getOnSiteVerifyItems()
        resetSaveOnSiteVerification()
        _filterStatusMessage.value = null
    }

}


data class OnsiteVerificationUiState(
    val allItemsFromApi: List<BoxItem> = emptyList(),
    val errorMessage: String = "",
    val itemsFromReader: List<BoxItem> = emptyList()
)
