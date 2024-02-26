package com.etag.stsyn.ui.screen.book_out.book_out

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.ClickEvent
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.etag.stsyn.enums.Purpose
import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.mapper.toItemMovementLog
import com.tzh.retrofit_module.data.mapper.toItemMovementLogs
import com.tzh.retrofit_module.data.model.book_in.PrintJob
import com.tzh.retrofit_module.data.model.book_in.SaveBookInRequest
import com.tzh.retrofit_module.data.settings.AppConfiguration
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import com.tzh.retrofit_module.domain.model.bookIn.safeCopy
import com.tzh.retrofit_module.domain.model.bookOut.BookOutResponse
import com.tzh.retrofit_module.domain.model.bookOut.GetAllBookOutBoxesResponse
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
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class BookOutViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler,
    private val bookOutRepository: BookOutRepository,
    private val localDataStore: LocalDataStore,
    private val appConfiguration: AppConfiguration
) : BaseViewModel(rfidHandler) {

    companion object {
        private const val TAG = "BookOutViewModel"
    }

    private val _bookOutUiState = MutableStateFlow(BookOutUiState())
    val bookOutUiState: StateFlow<BookOutUiState> = _bookOutUiState.asStateFlow()

    private val _getAllBookOutItemResponse =
        MutableStateFlow<ApiResponse<BookOutResponse>>(ApiResponse.Default)
    val getAllBookOutItemResponse: StateFlow<ApiResponse<BookOutResponse>> =
        _getAllBookOutItemResponse.asStateFlow()

    private val _getAllBookOutBoxesResponse = MutableStateFlow<ApiResponse<GetAllBookOutBoxesResponse>> (ApiResponse.Default)
    val getAllBookOutBoxesResponse: StateFlow<ApiResponse<GetAllBookOutBoxesResponse>> = _getAllBookOutBoxesResponse.asStateFlow()

    private val _saveBookOutBoxesResponse = MutableStateFlow<ApiResponse<NormalResponse>>(ApiResponse.Default)
    val saveBookOutBoxesResponse: StateFlow<ApiResponse<NormalResponse>> = _saveBookOutBoxesResponse.asStateFlow()

    val settings = appConfiguration.appConfig
    val user = localDataStore.getUser

    init {
        getAllBookOutItems()
        handleClickEvent()
        observeBookOutItemsResponse()

        // Testing
        viewModelScope.launch {
            bookOutUiState.collect {
                if (it.allBookOutItems.size > 1) _bookOutUiState.update { uiState ->
                    uiState.copy(scannedItems = it.allBookOutItems.subList(0,1))
                }
            }
        }
    }

    private fun handleClickEvent() {
        viewModelScope.launch {
            clickEventFlow.collect {
                when (it) {
                    is ClickEvent.ClickAfterSave -> doTasksAfterSavingItems()
                    is ClickEvent.RetryClick -> getAllBookOutItems()
                    else -> {}
                }
            }
        }
    }

    private fun doTasksAfterSavingItems() {
        viewModelScope.launch {
            updateSuccessDialogVisibility(false)
            getAllBookOutItems()
            _bookOutUiState.update { it.copy(allBookOutItems = emptyList(), scannedItems = emptyList(), purpose = "", location = "") }
        }
    }

    private fun observeBookOutItemsResponse() {
        viewModelScope.launch {
            getAllBookOutItemResponse.collect {
                handleDialogStatesByResponse(it)
            }
        }
    }

    private fun getAllBookOutItems() {
        viewModelScope.launch {
            _getAllBookOutItemResponse.value = ApiResponse.Loading
            delay(500)
            _getAllBookOutItemResponse.value = bookOutRepository.getAllBookOutItems()
            when (_getAllBookOutItemResponse.value) {
                is ApiResponse.Success -> {
                    val allItems = (_getAllBookOutItemResponse.value as ApiResponse.Success<BookOutResponse>).data?.items ?: emptyList()
                    _bookOutUiState.update { it.copy( allBookOutItems = allItems.map { it.safeCopy() } ) }
                }

                else -> {}
            }
        }
    }

    override fun onReceivedTagId(id: String) {
        Log.d(TAG, "onReceivedTagId: $id")
        addScannedItem(id)
    }

    fun setPurpose (purpose: String) {
        _bookOutUiState.update { it.copy(purpose = purpose) }
    }

    fun setBookOutErrorMessage(errorMessage: String?) {
        _bookOutUiState.update { it.copy(errorMessage = errorMessage) }
    }

    fun setLocation(location: String) {
        _bookOutUiState.update { it.copy(location = location) }
    }

    fun clearAllScannedItems() {
        _bookOutUiState.update { it.copy(scannedItems = emptyList()) }
    }

    fun isUnderCalibrationAlert(calDate: String?) = if (calDate != null) DateUtil.isUnderCalibrationAlert(calDate) else false

    fun removeScannedItem(item: BoxItem){
        val currentList = bookOutUiState.value.scannedItems.toMutableList()
        currentList.remove(item)
        _bookOutUiState.update { it.copy(scannedItems = currentList) }
    }

    fun saveBookOutItems() {
        viewModelScope.launch {
            val appConfig = settings.first()
            val user = user.first()

            val currentDate = DateUtil.getCurrentDate()
            bookOutUiState.value.scannedItems.forEach {
                if (it.calDate.isNotEmpty() && it.calDate != Instant.MIN.toString()) {
                    if (it.calDate < currentDate && bookOutUiState.value.purpose != Purpose.CALIBRATION.name) {
                        setBookOutErrorMessage("Include Over Due Calibration Item, Only Can Book Out For Calibration!")
                        return@launch
                    }
                } else if (it.calDate.isEmpty()) {
                    setBookOutErrorMessage("Include invalid calibration date!")
                    return@launch
                }
            }

            if (settings.first().needLocation) {
                if (bookOutUiState.value.location.isEmpty() && bookOutUiState.value.purpose.isEmpty()) {
                    setBookOutErrorMessage("Please Key In Location")
                    return@launch
                }
            }

            _saveBookOutBoxesResponse.value = ApiResponse.Loading
            val printJob = PrintJob(
                date = currentDate,
                handheldId = appConfig.handheldReaderId.toInt(),
                reportType = bookOutUiState.value.purpose,
                userId = user.userId.toInt()
            )

            val itemMovementLogs = bookOutUiState.value.scannedItems.map { it.toItemMovementLog(
                readerId = appConfig.handheldReaderId,
                date = currentDate,
                issuerId = user.userId,
                workLocation = bookOutUiState.value.location,
                itemStatus = bookOutUiState.value.purpose
            ) }

            _saveBookOutBoxesResponse.value = bookOutRepository.saveBookOutItems(
                SaveBookInRequest(
                    printJob = printJob,
                    itemMovementLogs = itemMovementLogs
                )
            )

            when (_saveBookOutBoxesResponse.value) {
                is ApiResponse.Success -> updateSuccessDialogVisibility(true)
                else -> {}
            }
        }
    }

    private fun addScannedItem(id: String) {
        val hasExisted = id in bookOutUiState.value.scannedItems.map { it.epc }

        try {
            val scannedItem = bookOutUiState.value.allBookOutItems.find { it.epc == id }
            if (!hasExisted) {
                if (scannedItem != null) {
                    _bookOutUiState.update {
                        val updatedItems = it.scannedItems.toMutableList()
                        updatedItems.add(scannedItem!!)
                        it.copy(scannedItems = updatedItems)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    data class BookOutUiState(
        val allBookOutItems: List<BoxItem> = listOf(),
        val purpose: String = "",
        val location: String = "",
        val errorMessage: String? = null,
        val scannedItems: List<BoxItem> = emptyList()
    )
}