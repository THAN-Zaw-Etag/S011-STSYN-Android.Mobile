package com.etag.stsyn.ui.screen.book_out.book_out

import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.etag.stsyn.enums.Purpose
import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.mapper.toItemMovementLogs
import com.tzh.retrofit_module.data.model.book_in.PrintJob
import com.tzh.retrofit_module.data.model.book_in.SaveBookInRequest
import com.tzh.retrofit_module.data.settings.AppConfiguration
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import com.tzh.retrofit_module.domain.model.bookOut.BookOutResponse
import com.tzh.retrofit_module.domain.model.bookOut.GetAllBookOutBoxesResponse
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.repository.BookOutRepository
import com.tzh.retrofit_module.util.ApiResponse
import com.tzh.retrofit_module.util.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val TAG = "BookOut ViewModel"

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
    }

    private fun getAllBookOutItems() {
        viewModelScope.launch {
            _getAllBookOutItemResponse.value = bookOutRepository.getAllBookOutItems()
            when (_getAllBookOutItemResponse.value) {
                is ApiResponse.Success -> {
                    val allItems =
                        (_getAllBookOutItemResponse.value as ApiResponse.Success<BookOutResponse>).data?.items
                            ?: emptyList()
                    _bookOutUiState.update { it.copy(allBookOutItems = allItems) }
                }

                else -> {}
            }
        }
    }

    override fun onReceivedTagId(id: String) {
        addScannedItem(id)
    }

    fun updatePurpose (purpose: String) {
        _bookOutUiState.update { it.copy(purpose = purpose) }
    }

    fun updateBookOutErrorMessage(errorMessage: String?) {
        _bookOutUiState.update { it.copy(errorMessage = errorMessage) }
    }

    fun updateLocation(location: String) {
        _bookOutUiState.update { it.copy(location = location) }
    }

    fun clearAllScannedItems() {
        _bookOutUiState.update { it.copy(scannedItems = emptyList()) }
    }

    fun removeScannedItem(index: Int){
        val currentList = bookOutUiState.value.scannedItems
        currentList.toMutableList().removeAt(index)
        _bookOutUiState.update { it.copy(scannedItems = currentList) }
    }

    fun saveBookOutItems() {
        viewModelScope.launch {
            val appConfig = settings.first()
            val user = user.first()
            val currentDate = DateUtil.getCurrentDate()
            bookOutUiState.value.scannedItems.forEach {
                if (it.calDate != null && it.calDate != Instant.MIN.toString()) {
                    if (it.calDate!! < currentDate && bookOutUiState.value.purpose != Purpose.CALIBRATION.name) {
                        updateBookOutErrorMessage("Include Over Due Calibration Item, Only Can Book Out For Calibration!")
                        return@launch
                    }
                }
            }

            if (settings.first().needLocation) {
                if (bookOutUiState.value.location.isEmpty() && bookOutUiState.value.purpose.isEmpty()) {
                    updateBookOutErrorMessage("Please Key In Location")
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

            val itemMovementLogs = bookOutUiState.value.scannedItems.toItemMovementLogs(
                handleHeldId = appConfig.handheldReaderId.toInt(),
                currentDate = currentDate,
                userId = user.userId,
                workLocation = bookOutUiState.value.location,
                itemStatus = bookOutUiState.value.purpose
            )

            _saveBookOutBoxesResponse.value = bookOutRepository.saveBookOutItems(
                SaveBookInRequest(
                    printJob = printJob,
                    itemMovementLogs = itemMovementLogs
                )
            )
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
        val scannedItems: List<BoxItem> = listOf(BoxItem())
    )
}