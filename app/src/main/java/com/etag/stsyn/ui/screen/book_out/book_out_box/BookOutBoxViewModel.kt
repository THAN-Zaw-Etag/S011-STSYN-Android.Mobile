package com.etag.stsyn.ui.screen.book_out.book_out_box

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.ClickEvent
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.etag.stsyn.enums.Purpose
import com.etag.stsyn.ui.screen.book_in.book_in_box.BoxUiState
import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.mapper.toItemMovementLog
import com.tzh.retrofit_module.data.model.book_in.ItemMovementLog
import com.tzh.retrofit_module.data.model.book_in.PrintJob
import com.tzh.retrofit_module.data.model.book_in.SaveBookInRequest
import com.tzh.retrofit_module.data.settings.AppConfiguration
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import com.tzh.retrofit_module.domain.model.bookOut.GetAllBookOutBoxesResponse
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.repository.BookOutRepository
import com.tzh.retrofit_module.enum.ItemStatus
import com.tzh.retrofit_module.util.ApiResponse
import com.tzh.retrofit_module.util.DateUtil
import com.tzh.retrofit_module.util.isBefore
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
class BookOutBoxViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler,
    private val appConfiguration: AppConfiguration,
    private val localDataStore: LocalDataStore,
    private val bookOutRepository: BookOutRepository
) : BaseViewModel(rfidHandler) {
    val TAG = "BookOutBoxViewModel"

    private val _boxUiState = MutableStateFlow(BoxUiState())
    val boxUiState: StateFlow<BoxUiState> = _boxUiState.asStateFlow()

    private val _getAllBookOutBoxesResponse =
        MutableStateFlow<ApiResponse<GetAllBookOutBoxesResponse>>(ApiResponse.Default)
    val getAllBookOutBoxesResponse: StateFlow<ApiResponse<GetAllBookOutBoxesResponse>> =
        _getAllBookOutBoxesResponse.asStateFlow()

    private val _bookOutBoxUiState = MutableStateFlow(BookOutBoxUiState())
    val bookOutBoxUiState: StateFlow<BookOutBoxUiState> = _bookOutBoxUiState.asStateFlow()

    private val _needLocation = MutableStateFlow(false)
    val needLocation: StateFlow<Boolean> = _needLocation.asStateFlow()

    private val _saveBookOutBoxResponse =
        MutableStateFlow<ApiResponse<NormalResponse>>(ApiResponse.Default)
    val saveBookOutBoxResponse: StateFlow<ApiResponse<NormalResponse>> =
        _saveBookOutBoxResponse.asStateFlow()

    private val _scannedItemList = MutableStateFlow<List<String>>(emptyList())
    val scannedItemList = _scannedItemList.asStateFlow()

    val user = localDataStore.getUser
    private val settings = appConfiguration.appConfig

    init {
        getAllBookOutBoxes()
        observeLocation()
        observeApiResponse()
        handleClickEvent()
    }

    private fun handleClickEvent() {
        viewModelScope.launch {
            clickEventFlow.collect {
                when (it) {
                    is ClickEvent.RetryClick -> getAllBookOutBoxes()
                    is ClickEvent.ClickAfterSave -> doTasksAfterSavingItems()
                    else -> {}
                }
            }
        }
    }

    private fun observeApiResponse() {
        viewModelScope.launch {
            getAllBookOutBoxesResponse.collect {
                handleDialogStatesByResponse(it)
            }
        }
    }

    private fun observeLocation() {
        viewModelScope.launch {
            settings.collect {
                _needLocation.value = it.needLocation
            }
        }
    }

    fun updateBookOutBoxErrorMessage(message: String?) {
        _bookOutBoxUiState.update { it.copy(errorMessage = message) }
    }

    private fun getAllBookOutBoxes() {
        viewModelScope.launch {
            _getAllBookOutBoxesResponse.value = ApiResponse.Loading
            _getAllBookOutBoxesResponse.value = bookOutRepository.getAllBookOutBoxes()
            delay(1000)
            when (getAllBookOutBoxesResponse.value) {
                is ApiResponse.Success -> {
                    val boxes =
                        (getAllBookOutBoxesResponse.value as ApiResponse.Success<GetAllBookOutBoxesResponse>).data?.items
                            ?: emptyList()
                    Log.d(TAG, "getAllBookOutBoxes: ${boxes.size}")
                    _boxUiState.update { it.copy(allBoxes = boxes) }
                }

                else -> {}
            }
        }
    }

    fun updateLocation(location: String) {
        _bookOutBoxUiState.update { it.copy(location = location) }
    }

    private fun doTasksAfterSavingItems() {
        viewModelScope.launch {
            updateSuccessDialogVisibility(false)
            getAllBookOutBoxes()
            _boxUiState.update { it.copy(scannedBox = BoxItem(), allItemsOfBox = emptyList()) }
            // clear all scanned items
            _scannedItemList.value = emptyList()
        }
    }

    fun saveBookOutBoxItems() {
        viewModelScope.launch {
            val scannedBox = boxUiState.value.scannedBox
            val purpose = bookOutBoxUiState.value.purpose
            val needLocation = settings.first().needLocation
            val location = bookOutBoxUiState.value.location
            val currentDate = DateUtil.getCurrentDate()

            if (scannedBox.calDate.isNotEmpty() && scannedBox.calDate != Instant.MIN.toString()) {
                if (scannedBox.calDate.isBefore(currentDate) && purpose != Purpose.CALIBRATION.name) {
                    updateBookOutBoxErrorMessage("Include Over Due Calibration Item, Only Can Book Out For Calibration!")
                    return@launch
                } else updateBookOutBoxErrorMessage(null)
            }

            if (needLocation) validateLocationAndPurpose(location, purpose)

            boxUiState.value.allItemsOfBox.forEach { box ->
                if (box.calDate.isNotEmpty() && box.calDate != Instant.MIN.toString()) {
                    if (box.calDate.isBefore(currentDate) && purpose != Purpose.CALIBRATION.name) {
                        updateBookOutBoxErrorMessage("Include Over Due Calibration Item, Only Can Book Out For Calibration!")
                        return@launch
                    } else if ((box.itemType == "TOOL" || box.itemType == "PUB") && purpose == Purpose.CALIBRATION.name) {
                        updateBookOutBoxErrorMessage(
                            "Include TOOLs or PUBs, can not book out as calibration."
                        )
                        return@launch
                    } else updateBookOutBoxErrorMessage(null)
                }
            }

            val printJob = PrintJob(
                date = currentDate,
                handheldId = settings.first().handheldReaderId.toInt(),
                reportType = purpose,
                userId = user.first().userId.toInt()
            )

            val itemMovementLogs = mutableListOf<ItemMovementLog>()
            // add scanned items
            itemMovementLogs.addAll(boxUiState.value.allItemsOfBox.filter { it.epc in scannedItemList.value }
                .map {
                    it.toItemMovementLog(
                        readerId = settings.first().handheldReaderId,
                        date = currentDate,
                        issuerId = user.first().userId,
                        workLocation = location,
                        itemStatus = purpose,
                        visualChecked = boxUiState.value.isChecked
                    )
                })

            // add outstanding items
            itemMovementLogs.addAll(boxUiState.value.allItemsOfBox.filter { it.epc !in scannedItemList.value }
                .map {
                    it.toItemMovementLog(
                        readerId = settings.first().handheldReaderId,
                        date = currentDate,
                        issuerId = user.first().userId,
                        workLocation = location,
                        itemStatus = ItemStatus.OUTSTANDING.name,
                        visualChecked = boxUiState.value.isChecked
                    )
                })

            if (boxUiState.value.allItemsOfBox.find { it.epc == scannedBox.epc } == null) {
                itemMovementLogs.add(
                    scannedBox.toItemMovementLog(
                        readerId = settings.first().handheldReaderId,
                        date = currentDate,
                        issuerId = user.first().userId,
                        workLocation = location,
                        itemStatus = purpose,
                        visualChecked = boxUiState.value.isChecked
                    )
                )
            }

            _saveBookOutBoxResponse.value = ApiResponse.Loading

            _saveBookOutBoxResponse.value = bookOutRepository.saveBookOutItems(
                SaveBookInRequest(
                    printJob = printJob,
                    itemMovementLogs = itemMovementLogs
                )
            )

        }
    }

    private fun validateLocationAndPurpose(location: String, purpose: String) {
        if (location.isEmpty() && purpose.isEmpty()) {
            updateBookOutBoxErrorMessage("Please Key In Location!")
            return
        } else updateBookOutBoxErrorMessage(null)
    }

    fun refreshScannedBox() {
        viewModelScope.launch {
            _scannedItemList.update { emptyList() }
            _boxUiState.update {
                it.copy(
                    scannedBox = BoxItem(), allItemsOfBox = mutableListOf()
                )
            }
        }
    }

    fun updatePurpose(purpose: String) {
        viewModelScope.launch {
            _bookOutBoxUiState.update { it.copy(purpose = purpose) }
        }
    }

    fun resetAllItemsInBox() {
        _scannedItemList.update { emptyList() }
        _boxUiState.update { it.copy(isChecked = false) }
    }

    fun toggleVisualCheck(enable: Boolean) {
        _boxUiState.update { it.copy(isChecked = enable) }
        if (enable) {
            _scannedItemList.update { _boxUiState.value.allItemsOfBox.map { it.epc } }
        } else _scannedItemList.update { emptyList() }
    }

    private fun getAllItemsInBox(box: String) {
        viewModelScope.launch {
            when (val response = bookOutRepository.getAllItemsInBookOutBox(box)) {
                is ApiResponse.Success -> {
                    _boxUiState.update {
                        it.copy(
                            allItemsOfBox = response.data?.items ?: emptyList()
                        )
                    }
                }

                else -> {}
            }
        }
    }

    override fun onReceivedTagId(id: String) {
        Log.d("TAG", "onReceivedTagId: $id")
        val scannedBox = boxUiState.value.allBoxes.find { it.epc == id }
        Log.d(TAG, "onReceivedTagId: $scannedBox")
        if (scannedBox != null && scannedBox.epc.isNotEmpty()) {
            _boxUiState.update { it.copy(scannedBox = scannedBox) }
            getAllItemsInBox(scannedBox.box)
        }
    }

    data class BookOutBoxUiState(
        val errorMessage: String? = null, val purpose: String = "", val location: String = ""
    )

}