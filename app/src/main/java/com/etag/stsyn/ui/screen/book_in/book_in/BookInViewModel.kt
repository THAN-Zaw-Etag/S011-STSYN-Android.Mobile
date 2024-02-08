package com.etag.stsyn.ui.screen.book_in.book_in

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.ClickEvent
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.mapper.toBookOutBoxItemMovementLog
import com.tzh.retrofit_module.data.model.book_in.PrintJob
import com.tzh.retrofit_module.data.model.book_in.SaveBookInRequest
import com.tzh.retrofit_module.data.settings.AppConfiguration
import com.tzh.retrofit_module.domain.model.bookIn.BookInResponse
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.repository.BookInRepository
import com.tzh.retrofit_module.enum.ItemStatus
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
class BookInViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler,
    localDataStore: LocalDataStore,
    appConfig: AppConfiguration,
    private val bookInRepository: BookInRepository
) : BaseViewModel(rfidHandler) {
    val TAG = "BookInViewModel"

    private val _bookInItemsResponse =
        MutableStateFlow<ApiResponse<BookInResponse>>(ApiResponse.Default)
    val bookInItemsResponse: StateFlow<ApiResponse<BookInResponse>> = _bookInItemsResponse

    private val _savedBookInResponse =
        MutableStateFlow<ApiResponse<NormalResponse>>(ApiResponse.Default)
    val savedBookInResponse: StateFlow<ApiResponse<NormalResponse>> = _savedBookInResponse

    private val _scannedItemIdList = MutableStateFlow<List<String>>(emptyList())
    val scannedItemIdList: StateFlow<List<String>> = _scannedItemIdList.asStateFlow()

    private val _bookInState = MutableStateFlow(BookInState())
    val bookInState: StateFlow<BookInState> = _bookInState.asStateFlow()

    val userFlow = localDataStore.getUser
    private val appConfigFlow = appConfig.appConfig

    init {
        getBookInItems()
        observeBookInItemsResponse()
        handleUiEvent()
    }

    private fun observeBookInItemsResponse() {
        viewModelScope.launch {
            delay(500)
            bookInItemsResponse.collect { handleDialogStatesByResponse(it) }
        }
    }

    private fun handleUiEvent() {
        viewModelScope.launch {
            clickEventFlow.collect {
                when (it) {
                    is ClickEvent.ClickAfterSave -> doTasksAfterSavingItems()
                    else -> {}
                }
            }
        }
    }

    override fun onReceivedTagId(id: String) {
        Log.d(TAG, "onReceivedTagId: $id")
        handleScannedItem(id)
    }

    private fun doTasksAfterSavingItems() {
        viewModelScope.launch {
            updateSuccessDialogVisibility(false)
            getBookInItems()
            _scannedItemIdList.update { emptyList() }
            _bookInState.update { it.copy(allBookInItems = emptyList()) }
        }
    }

    private fun handleScannedItem(id: String) {
        viewModelScope.launch {
            val hasExisted = id in scannedItemIdList.value
            val scannedItem = bookInState.value.allBookInItems.find { it.epc == id }
            if (!hasExisted) {
                if (scannedItem != null) {
                    _scannedItemIdList.update { it + id }
                }
            }
        }
    }

    fun removeScannedBookInItem(id: String) {
        viewModelScope.launch {
            _scannedItemIdList.update { it - id }
        }
    }

    fun removeAllScannedItems() {
        _bookInState.update { it.copy(allBookInItems = emptyList()) }
    }

    fun saveBookIn() {
        viewModelScope.launch {
            val currentDate = DateUtil.getCurrentDate()
            val setting = appConfigFlow.first()
            val userId = userFlow.map { it.userId }.first()

            val printJob = PrintJob(
                date = currentDate,
                handheldId = setting.handheldReaderId.toInt(),
                reportType = ItemStatus.BookIn.name,
                userId = userId.toInt()
            )

            val itemMovementLogs = bookInState.value.allBookInItems.map { item ->
                item.toBookOutBoxItemMovementLog(
                    itemStatus = ItemStatus.BookIn.name,
                    workLocation = "",
                    issuerId = item.issuerId,
                    date = currentDate,
                    readerId = setting.handheldReaderId,
                    visualChecked = false
                )
            }

            _savedBookInResponse.value = ApiResponse.Loading
            delay(1000)
            _savedBookInResponse.value = bookInRepository.saveBookIn(
                saveBookInRequest = SaveBookInRequest(
                    printJob = printJob, itemMovementLogs = itemMovementLogs
                )
            )

            when (savedBookInResponse.value){
                is ApiResponse.Success -> updateSuccessDialogVisibility(true)
                else -> {}
            }
        }
    }

    fun getBoxItemsForBookIn() {
        viewModelScope.launch {

        }
    }

    private fun getBookInItems() {
        viewModelScope.launch {
            _bookInItemsResponse.value = ApiResponse.Loading
            delay(1000)
            _bookInItemsResponse.value = bookInRepository.getBookInItems()

            when (bookInItemsResponse.value) {
                is ApiResponse.Success -> {
                    _bookInState.update {
                        it.copy(allBookInItems = (bookInItemsResponse.value as ApiResponse.Success<BookInResponse>).data?.items!!)
                    }
                }

                else -> {}
            }
        }
    }

    data class BookInState(
        val allBookInItems: List<BoxItem> = emptyList(),
    )
}