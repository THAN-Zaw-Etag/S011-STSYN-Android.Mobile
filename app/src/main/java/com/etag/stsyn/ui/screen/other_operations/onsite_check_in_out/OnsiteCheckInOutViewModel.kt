package com.etag.stsyn.ui.screen.other_operations.onsite_check_in_out

import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.UiEvent
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.mapper.toBookOutBoxItemMovementLog
import com.tzh.retrofit_module.data.model.book_in.PrintJob
import com.tzh.retrofit_module.data.model.book_in.SaveBookInRequest
import com.tzh.retrofit_module.data.settings.AppConfiguration
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.model.onsiteCheckInOut.GetItemsForOnsiteResponse
import com.tzh.retrofit_module.domain.model.user.UserModel
import com.tzh.retrofit_module.domain.repository.CheckIn.CheckInOutRepository
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
class OnsiteCheckInOutViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler,
    private val localDataStore: LocalDataStore,
    private val appConfiguration: AppConfiguration,
    private val repository: CheckInOutRepository
) : BaseViewModel(rfidHandler) {
    val TAG = "OnsiteCheckInOutViewModel"

    private val _getAllItemsForOnsiteResponse =
        MutableStateFlow<ApiResponse<GetItemsForOnsiteResponse>>(ApiResponse.Default)
    val getAllItemsForOnsiteResponse: StateFlow<ApiResponse<GetItemsForOnsiteResponse>> =
        _getAllItemsForOnsiteResponse.asStateFlow()

    private val _onSiteCheckInOutUiState = MutableStateFlow(OnsiteCheckInOutUiState())
    val onSiteCheckInOutUiState: StateFlow<OnsiteCheckInOutUiState> =
        _onSiteCheckInOutUiState.asStateFlow()

    private val _saveOnSiteCheckInOutResponse =
        MutableStateFlow<ApiResponse<NormalResponse>>(ApiResponse.Default)
    val saveOnSiteCheckInOutResponse: StateFlow<ApiResponse<NormalResponse>> =
        _saveOnSiteCheckInOutResponse.asStateFlow()

    private val _scannedItemList = MutableStateFlow<List<String>>(emptyList())
    val scannedItemList: StateFlow<List<String>> = _scannedItemList.asStateFlow()

    val userFlow = localDataStore.getUser
    private val settingsFlow = appConfiguration.appConfig

    init {
        getAllItemsForOnsite()

        viewModelScope.launch {
            eventFlow.collect {event ->
                when (event) {
                    is UiEvent.ClickAfterSave -> doTasksAfterSaved()
                    else -> {}
                }
            }
        }
    }

    private suspend fun doTasksAfterSaved() {
        _scannedItemList.value = emptyList()
        _onSiteCheckInOutUiState.update { it.copy(allItemsForOnsite = emptyList(), receiver = null) }
        delay(1000)
        getAllItemsForOnsite()
    }

    fun saveOnsiteCheckInOut() {
        viewModelScope.launch {
            _saveOnSiteCheckInOutResponse.value = ApiResponse.Loading

            val allItems = onSiteCheckInOutUiState.value.allItemsForOnsite
            val user = userFlow.first()
            val settings = settingsFlow.first()
            val currentDate = DateUtil.getCurrentDate()

            val printJob = PrintJob(
                date = currentDate,
                handheldId = settings.handheldReaderId.toInt(),
                reportType = "",
                userId = user.userId.toInt()
            )

            val itemMovementLogs = allItems.filter { it.epc in scannedItemList.value }.map {
                it.toBookOutBoxItemMovementLog(
                    itemStatus = it.itemStatus,
                    workLocation = it.workLocation,
                    issuerId = it.issuerId,
                    date = currentDate,
                    readerId = settings.handheldReaderId,
                    visualChecked = false
                )
            }

            _saveOnSiteCheckInOutResponse.value = repository.saveOnsiteCheckInOut(SaveBookInRequest(
                printJob = printJob,
                itemMovementLogs = itemMovementLogs
            ))
        }
    }

    fun getAllItemsForOnsite() {
        viewModelScope.launch {
            _getAllItemsForOnsiteResponse.value = ApiResponse.Loading
            delay(1000)
            _getAllItemsForOnsiteResponse.value = repository.getItemsForOnSite()

            when (_getAllItemsForOnsiteResponse.value) {
                is ApiResponse.Success -> {
                    _onSiteCheckInOutUiState.update {
                        it.copy(
                            allItemsForOnsite = (_getAllItemsForOnsiteResponse.value as ApiResponse.Success<GetItemsForOnsiteResponse>).data?.items
                                ?: emptyList()
                        )
                    }
                }

                else -> {}
            }
        }
    }

    fun updateOnsiteScanType(scanType: OnSiteScanType) {
        _onSiteCheckInOutUiState.update { it.copy(scanType = scanType) }
    }

    fun removeScannedItem(id: String) {
        _scannedItemList.update { it - id }
    }

    fun removeAllScannedItems() {
        _scannedItemList.update { emptyList() }
    }

    fun updateWarningDialogVisibility(visible: Boolean) {
        _onSiteCheckInOutUiState.update { it.copy(shouldShowWarningDialog = visible) }
    }

    fun updateOnsiteCheckInOutErrorMessage(errorMessage: String?) {
        _onSiteCheckInOutUiState.update { it.copy(errorMessage = errorMessage) }
    }

    override fun onReceivedTagId(id: String) {
        viewModelScope.launch {
            when (onSiteCheckInOutUiState.value.scanType) {
                OnSiteScanType.ITEMS -> {
                    val allItems = onSiteCheckInOutUiState.value.allItemsForOnsite
                    val scannedItem = allItems.find { it.epc == id }
                    if (scannedItem != null) {
                        val hasExisted = (id in scannedItemList.value)
                        if (!hasExisted) {
                            if (scannedItem.receiverId == "0" && scannedItem.issuerId != userFlow.first().userId) {
                                updateWarningDialogVisibility(true)
                                return@launch
                            } else updateWarningDialogVisibility(false)

                            if (scannedItem.receiverId != "0" && scannedItem.receiverId != userFlow.first().userId) {
                                updateWarningDialogVisibility(true)
                                return@launch
                            } else updateWarningDialogVisibility(false)

                            _scannedItemList.update { it + id }
                        } else {
                            updateWarningDialogVisibility(false)
                        }
                    }
                }

                OnSiteScanType.RECEIVER -> {
                    // get receiver by id from api
                    when (val response = repository.getReceiverByEpc(id)) {
                        is ApiResponse.Success -> {
                            _onSiteCheckInOutUiState.update { it.copy(receiver = response.data?.userModel) }
                        }

                        is ApiResponse.AuthorizationError -> shouldShowAuthorizationFailedDialog(
                            true
                        )

                        else -> {}
                    }
                }
            }
        }
    }

    data class OnsiteCheckInOutUiState(
        val allItemsForOnsite: List<BoxItem> = emptyList(),
        val shouldShowWarningDialog: Boolean = false,
        val errorMessage: String? = null,
        val receiver: UserModel? = null,
        val scanType: OnSiteScanType = OnSiteScanType.ITEMS
    )

    enum class OnSiteScanType { ITEMS, RECEIVER }
}