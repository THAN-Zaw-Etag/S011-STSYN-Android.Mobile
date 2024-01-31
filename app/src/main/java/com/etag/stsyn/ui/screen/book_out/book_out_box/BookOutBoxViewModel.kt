package com.etag.stsyn.ui.screen.book_out.book_out_box

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.etag.stsyn.ui.screen.book_in.book_in_box.BoxUiState
import com.tzh.retrofit_module.data.settings.AppConfiguration
import com.tzh.retrofit_module.domain.repository.BookOutRepository
import com.tzh.retrofit_module.util.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookOutBoxViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler,
    private val appConfiguration: AppConfiguration,
    private val bookOutRepository: BookOutRepository
) : BaseViewModel(rfidHandler) {
    private val _boxUiState = MutableStateFlow(BoxUiState())
    val boxUiState: StateFlow<BoxUiState> = _boxUiState.asStateFlow()

    private val _needLocation = MutableStateFlow(false)
    val needLocation: StateFlow<Boolean> = _needLocation.asStateFlow()

    private val settings = appConfiguration.appConfig

    init {
        getAllBookOutBoxes()
        viewModelScope.launch {
            settings.collect {
                _needLocation.value = it.needLocation
            }
        }
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

    private fun getAllItemsInBox(box: String) {
        viewModelScope.launch {
            when (val response = bookOutRepository.getAllItemsInBookOutBox(box)) {
                is ApiResponse.Success -> {_boxUiState.update { it.copy(allItemsOfBox = response.data?.items ?: emptyList()) }}
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


}