package com.etag.stsyn.ui.screen.book_out.book_out_box

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.etag.stsyn.enums.Purpose
import com.etag.stsyn.ui.screen.book_in.book_in_box.BoxUiState
import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.settings.AppConfiguration
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import com.tzh.retrofit_module.domain.repository.BookOutRepository
import com.tzh.retrofit_module.util.ApiResponse
import com.tzh.retrofit_module.util.DateUtil
import com.tzh.retrofit_module.util.isBefore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach
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

    private val _bookOutBoxUiState = MutableStateFlow(BookOutBoxUiState())
    val bookOutBoxUiState: StateFlow<BookOutBoxUiState> = _bookOutBoxUiState.asStateFlow()

    private val _needLocation = MutableStateFlow(false)
    val needLocation: StateFlow<Boolean> = _needLocation.asStateFlow()

    val scannedItemList = MutableStateFlow<List<String>>(emptyList())

    val user = localDataStore.getUser
    private val settings = appConfiguration.appConfig

    init {
        getAllBookOutBoxes()
        viewModelScope.launch {
            settings.collect {
                _needLocation.value = it.needLocation
            }
        }
    }

    fun saveBookOutBoxItems() {

    }

    fun updateBookOutBoxErrorMessage(message: String?) {
        _bookOutBoxUiState.update { it.copy(errorMessage = message) }
    }

    private fun getAllBookOutBoxes() {
        viewModelScope.launch {
            val response = bookOutRepository.getAllBookOutBoxes()
            toggleLoadingVisibility(true)
            delay(1000)
            when (response) {
                is ApiResponse.Loading -> {}
                is ApiResponse.Success -> {
                    toggleLoadingVisibility(false)
                    _boxUiState.update { it.copy(allBoxes = response.data?.items ?: emptyList()) }
                }
                is ApiResponse.ApiError -> {
                    toggleLoadingVisibility(false)
                    updateErrorMessage(response.message)
                }
                is ApiResponse.AuthorizationError -> {
                    toggleLoadingVisibility(false)
                    shouldShowAuthorizationFailedDialog(true)
                }
                else -> {}
            }
        }
    }

    fun updateLocation (location: String) {
        _bookOutBoxUiState.update { it.copy(location = location) }
    }

    fun saveBookOutBoxRequest() {
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

            if (needLocation) {
                if (location.isEmpty() && purpose.isEmpty()) {
                    updateBookOutBoxErrorMessage("Please Key In Location!")
                    return@launch
                }else updateBookOutBoxErrorMessage(null)
            }

            boxUiState.value.allItemsOfBox.forEach {box ->
                if (box.calDate.isNotEmpty() && box.calDate != Instant.MIN.toString()) {
                    if (box.calDate.isBefore(currentDate) && purpose != Purpose.CALIBRATION.name) {
                        updateBookOutBoxErrorMessage("Include Over Due Calibration Item, Only Can Book Out For Calibration!")
                    }
                }
            }
        }
    }

    fun refreshScannedBox() {
        viewModelScope.launch {
            scannedItemList.update { emptyList() }
            _boxUiState.update {
                it.copy(scannedBox = BoxItem(), allBoxes = emptyList(), allItemsOfBox = mutableListOf())
            }
        }
    }

    fun updatePurpose (purpose: String) {
        viewModelScope.launch {
            _bookOutBoxUiState.update { it.copy(purpose = purpose) }
        }
    }

    fun resetAllItemsInBox() {
        scannedItemList.update { emptyList() }
        _boxUiState.update { it.copy(isChecked = false) }
    }

    fun toggleVisualCheck(enable: Boolean) {
        _boxUiState.update { it.copy(isChecked = enable) }
        if (enable) {
            scannedItemList.update { _boxUiState.value.allItemsOfBox.map { it.epc } }
        } else scannedItemList.update { emptyList() }
    }

    private fun getAllItemsInBox(box: String) {
        viewModelScope.launch {
            when (val response = bookOutRepository.getAllItemsInBookOutBox(box)) {
                is ApiResponse.Success -> {
                    _boxUiState.update { it.copy(allItemsOfBox = response.data?.items ?: emptyList()) }
                }
                else -> {}
            }
        }
    }

    override fun onReceivedTagId(id: String) {
        Log.d("TAG", "onReceivedTagId: $id")
        val scannedBox = boxUiState.value.allBoxes.find { it.epc == id }

        if (scannedBox != null) {
            _boxUiState.update { it.copy(scannedBox = scannedBox) }
            getAllItemsInBox(scannedBox.box)
        }
    }

    data class BookOutBoxUiState(
        val errorMessage: String? = null,
        val purpose: String = "",
        val location: String = ""
    )

}