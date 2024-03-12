package com.etag.stsyn.ui.screen.acct_check

import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.ClickEvent
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.etag.stsyn.ui.screen.login.Shift
import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.mapper.toAccountabilityCheckRequest
import com.tzh.retrofit_module.data.mapper.toFilterList
import com.tzh.retrofit_module.data.mapper.toStockTake
import com.tzh.retrofit_module.data.model.account_check.AccountCheckOutstandingItemsRequest
import com.tzh.retrofit_module.data.model.account_check.SaveAccountabilityCheckRequest
import com.tzh.retrofit_module.data.settings.AppConfiguration
import com.tzh.retrofit_module.domain.model.FilterItem
import com.tzh.retrofit_module.domain.model.accountabilityCheck.GetAllAccountabilityCheckItemsResponse
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.repository.AccountCheckRepository
import com.tzh.retrofit_module.util.ApiResponse
import com.tzh.retrofit_module.util.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountCheckViewModel @Inject constructor(
    private val rfidHandler: ZebraRfidHandler,
    private val accountCheckRepository: AccountCheckRepository,
    private val localDataStore: LocalDataStore,
    private val appConfiguration: AppConfiguration
) : BaseViewModel(rfidHandler) {
    companion object {
        const val TAG = "AccountCheckViewModel"
    }

    private val _acctCheckUiState = MutableStateFlow(AcctCheckUiState())
    val acctCheckUiState: StateFlow<AcctCheckUiState> = _acctCheckUiState.asStateFlow()

    private val _accountabilityCheckItemsResponse =
        MutableStateFlow<ApiResponse<GetAllAccountabilityCheckItemsResponse>>(ApiResponse.Default)
    val accountabilityCheckItemsResponse: StateFlow<ApiResponse<GetAllAccountabilityCheckItemsResponse>> =
        _accountabilityCheckItemsResponse.asStateFlow()

    private val _saveAcctCheckResponse =
        MutableStateFlow<ApiResponse<NormalResponse>>(ApiResponse.Default)
    val saveAcctCheckResponse: StateFlow<ApiResponse<NormalResponse>> =
        _saveAcctCheckResponse.asStateFlow()

    private val _scannedItemIdList = MutableStateFlow<List<String>>(emptyList())
    val scannedItemIdList: StateFlow<List<String>> = _scannedItemIdList.asStateFlow()

    private val checkUserIdFlow = localDataStore.checkStatusId
    val userFlow = localDataStore.getUser
    private val settingFlow = appConfiguration.appConfig

    init {
        updateScanType(ScanType.Single)
        getAllAccountabilityCheckItems()
        getAllFilterOptions()
    }

    private fun observeAccountabilityCheckSaveResponse() {
        viewModelScope.launch {
            saveAcctCheckResponse.collect {
                handleDialogStatesByResponse(it, true)
            }
        }
    }

    override fun handleApiResponse() {
        super.handleApiResponse()
        observeAccountabilityCheckResponse()
        observeAccountabilityCheckSaveResponse()
    }

    override fun handleClickEvent(clickEvent: ClickEvent) {
        super.handleClickEvent(clickEvent)
        when (clickEvent) {
            is ClickEvent.RetryClick -> getAllAccountabilityCheckItems()
            is ClickEvent.ClickAfterSave -> updateClickEvent(ClickEvent.ClickToNavigateHome)
            else -> {}
        }
    }

    fun clearFilters() {
        _acctCheckUiState.update { uiState ->
            val updatedList = uiState.filterOptions.map { it.copy(selectedOption = "") }
            uiState.copy(filterOptions = updatedList)
        }
    }

    fun updateFilterOptions(filterOptions: List<FilterItem>, isUpdateAll: Boolean) {
        _acctCheckUiState.update {
            it.copy(
                filterOptions = filterOptions,
                isUpdateAll = isUpdateAll,
                acctCheckRequest = filterOptions.toAccountabilityCheckRequest()
            )
        }
        getAllAccountabilityCheckItems()
    }

    private fun observeAccountabilityCheckResponse() {
        viewModelScope.launch {
            accountabilityCheckItemsResponse.collect {
                handleDialogStatesByResponse(it)
            }
        }
    }

    private fun getAllFilterOptions() {
        viewModelScope.launch {
            when (val response = accountCheckRepository.getAllFilterOptions()) {
                is ApiResponse.Success -> {
                    _acctCheckUiState.update {
                        it.copy(
                            filterOptions = response.data?.dropdownSet?.toFilterList()
                                ?: emptyList()
                        )
                    }
                }

                else -> {}
            }
        }
    }

    private fun getAllAccountabilityCheckItems() {
        viewModelScope.launch {
            _accountabilityCheckItemsResponse.value = ApiResponse.Loading
            delay(500)
            val accountCheckOutstandingItemsRequest = acctCheckUiState.value.acctCheckRequest
            _accountabilityCheckItemsResponse.value =
                accountCheckRepository.getAllAccountabilityCheckItems(
                    accountCheckOutstandingItemsRequest
                )

            when (accountabilityCheckItemsResponse.value) {
                is ApiResponse.Success -> {
                    _acctCheckUiState.update {
                        it.copy(allItems = (accountabilityCheckItemsResponse.value as ApiResponse.Success<GetAllAccountabilityCheckItemsResponse>).data!!.items)
                    }
                }

                else -> {}
            }
        }
    }

    fun saveAccountabilityCheck() {
        viewModelScope.launch {
            val currentDate = DateUtil.getCurrentDate()
            val userId = userFlow.map { it.userId }.first()
            val readerId = settingFlow.map { it.handheldReaderId }.first()
            val shift = acctCheckUiState.value.shiftType.toString()
            val checkStatusId = checkUserIdFlow.first() ?: 0

            _saveAcctCheckResponse.value = ApiResponse.Loading
            delay(500)
            _saveAcctCheckResponse.value = ApiResponse.Success(NormalResponse(null, true))
            SaveAccountabilityCheckRequest(acctCheckUiState.value.allItems.filter { it.epc in scannedItemIdList.value }
                .map {
                    it.toStockTake(
                        readerId = readerId,
                        userId = userId,
                        date = currentDate,
                        shift = shift,
                        checkStatusId = checkStatusId.toString()
                    )
                })
        }
    }

    override fun onReceivedTagId(id: String) {
        viewModelScope.launch {
            val scannedItem = acctCheckUiState.value.allItems.find { id == it.epc }
            val hasExisted = id in scannedItemIdList.value
            if (scannedItem != null && !hasExisted) {
                _acctCheckUiState.update { it.copy(scannedItem = scannedItem, unknownEpc = null) }
                addScannedItemId(id)
            } else {
                _acctCheckUiState.update {
                    it.copy(
                        unknownEpc = id,
                        showUnknownEpcDialog = acctCheckUiState.value.isUpdateAll
                    )
                }
            }
        }
    }

    fun hideUnknownEpcDialog() {
        _acctCheckUiState.update { it.copy(unknownEpc = null) }
    }

    fun resetScannedItems() {
        _scannedItemIdList.update { emptyList() }
        _acctCheckUiState.update { it.copy(scannedItem = BoxItem()) }
    }

    private fun addScannedItemId(id: String) {
        _scannedItemIdList.update { it + id }
    }

    fun setShiftType(shiftType: Shift) {
        _acctCheckUiState.update { it.copy(shiftType = shiftType) }
    }

    data class AcctCheckUiState(
        val shiftType: Shift = Shift.START,
        val allItems: List<BoxItem> = emptyList(),
        val unknownEpc: String? = null,
        val showUnknownEpcDialog: Boolean = false,
        val isUpdateAll: Boolean = false,
        val scannedItem: BoxItem = BoxItem(),
        val filterOptions: List<FilterItem> = emptyList(),
        val acctCheckRequest: AccountCheckOutstandingItemsRequest = AccountCheckOutstandingItemsRequest()
    )
}