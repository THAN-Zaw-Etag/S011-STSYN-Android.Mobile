package com.etag.stsyn.ui.screen.acct_check

import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.ClickEvent
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.etag.stsyn.ui.screen.login.Shift
import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.mapper.toFilterList
import com.tzh.retrofit_module.data.model.account_check.AccountCheckOutstandingItemsRequest
import com.tzh.retrofit_module.data.model.account_check.SaveAccountabilityCheckRequest
import com.tzh.retrofit_module.data.settings.AppConfiguration
import com.tzh.retrofit_module.domain.model.FilterItem
import com.tzh.retrofit_module.domain.model.accountabilityCheck.DropdownSet
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

    private val _acctCheckUiState = MutableStateFlow(AcctCheckUiState())
    val acctCheckUiState: StateFlow<AcctCheckUiState> = _acctCheckUiState.asStateFlow()

    private val _accountabilityCheckItemsResponse = MutableStateFlow<ApiResponse<GetAllAccountabilityCheckItemsResponse>>(ApiResponse.Default)
    val accountabilityCheckItemsResponse: StateFlow<ApiResponse<GetAllAccountabilityCheckItemsResponse>> = _accountabilityCheckItemsResponse.asStateFlow()

    private val saveAcctCheckResponse = MutableStateFlow<ApiResponse<NormalResponse>>(ApiResponse.Default)

    private val checkUserIdFlow = localDataStore.checkStatusId
    private val userFlow = localDataStore.getUser
    private val settingFlow = appConfiguration.appConfig

    init {
        updateScanType(ScanType.Single)
        getAllAccountabilityCheckItems()
        getAllFilterOptions()
        observeAccountabilityCheckResponse()
        handleUiEvent()
    }

    private fun handleUiEvent() {
        viewModelScope.launch {
            eventFlow.collect {
                when (it) {
                    is ClickEvent.RetryClick -> getAllAccountabilityCheckItems()
                    is ClickEvent.ClickAfterSave -> doTasksAfterSaving()
                    else -> {}
                }
            }
        }
    }

    private fun doTasksAfterSaving() {

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
                        it.copy(filterOptions = response.data?.dropdownSet?.toFilterList() ?: emptyList())
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
            _accountabilityCheckItemsResponse.value = accountCheckRepository.getAllAccountabilityCheckItems(accountCheckOutstandingItemsRequest)

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
            val checkStatusId = checkUserIdFlow.first()

            saveAcctCheckResponse.value = ApiResponse.Loading
            delay(500)
            saveAcctCheckResponse.value = accountCheckRepository.saveAccountabilityCheck(
                SaveAccountabilityCheckRequest(emptyList())
            )
        }
    }

    override fun onReceivedTagId(id: String) {

    }

    fun setShiftType(shiftType: Shift) {
        _acctCheckUiState.update { it.copy(shiftType = shiftType) }
    }

    data class AcctCheckUiState(
        val shiftType: Shift = Shift.START,
        val allItems: List<BoxItem> = emptyList(),
        val filterOptions: List<FilterItem> = emptyList(),
        val acctCheckRequest: AccountCheckOutstandingItemsRequest = AccountCheckOutstandingItemsRequest()
    )
}