package com.etag.stsyn.ui.screen.other_operations.onsite_check_in_out

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import com.tzh.retrofit_module.domain.model.bookIn.GetAllItemsOfBoxResponse
import com.tzh.retrofit_module.domain.model.onsiteCheckInOut.GetItemsForOnsiteResponse
import com.tzh.retrofit_module.domain.repository.CheckIn.CheckInOutRepository
import com.tzh.retrofit_module.util.ApiResponse
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
class OnsiteCheckInOutViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler,
    private val localDataStore: LocalDataStore,
    private val repository: CheckInOutRepository
) : BaseViewModel(rfidHandler) {
    val TAG = "OnsiteCheckInOutViewModel"

    private val _getAllItemsForOnsiteResponse = MutableStateFlow<ApiResponse<GetItemsForOnsiteResponse>>(ApiResponse.Default)
    val getAllItemsForOnsiteResponse: StateFlow<ApiResponse<GetItemsForOnsiteResponse>> = _getAllItemsForOnsiteResponse.asStateFlow()

    private val _onSiteCheckInOutUiState = MutableStateFlow(OnsiteCheckInOutUiState())
    val onSiteCheckInOutUiState: StateFlow<OnsiteCheckInOutUiState> = _onSiteCheckInOutUiState.asStateFlow()

    private val _scannedItemList = MutableStateFlow<List<String>>(emptyList())
    val scannedItemList: StateFlow<List<String>> = _scannedItemList.asStateFlow()

    val user = localDataStore.getUser

    init {
        getAllItemsForOnsite()
    }

    fun getAllItemsForOnsite() {
        viewModelScope.launch {
            _getAllItemsForOnsiteResponse.value = ApiResponse.Loading
            delay(1000)
            _getAllItemsForOnsiteResponse.value = repository.getItemsForOnSite()

            when (_getAllItemsForOnsiteResponse.value) {
                is ApiResponse.Success -> {
                    _onSiteCheckInOutUiState.update { it.copy(
                        allItemsForOnsite = (_getAllItemsForOnsiteResponse.value as ApiResponse.Success<GetItemsForOnsiteResponse>).data?.items ?: emptyList()
                    ) }
                }
                else -> {}
            }
        }
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

    override fun onReceivedTagId(id: String) {
        viewModelScope.launch {
            val allItems = onSiteCheckInOutUiState.value.allItemsForOnsite
            val scannedItem = allItems.find { it.epc == id }

            if (scannedItem != null) {
                val hasExisted = (id in scannedItemList.value)
                if (!hasExisted) {
                    if (scannedItem.receiverId == "0" && scannedItem.issuerId != user.first().userId) {
                        updateWarningDialogVisibility(true)
                        return@launch
                    } else updateWarningDialogVisibility(false)

                    if (scannedItem.receiverId != "0" && scannedItem.receiverId != user.first().userId) {
                        updateWarningDialogVisibility(true)
                        return@launch
                    } else updateWarningDialogVisibility(false)

                    _scannedItemList.update { it + id }
                } else {
                    updateWarningDialogVisibility(false)
                }
            }
        }
    }

    data class OnsiteCheckInOutUiState(
        val allItemsForOnsite: List<BoxItem> = emptyList(),
        val shouldShowWarningDialog: Boolean = false,
    )
}