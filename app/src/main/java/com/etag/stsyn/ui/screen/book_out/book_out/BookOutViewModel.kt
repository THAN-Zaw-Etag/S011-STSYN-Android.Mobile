package com.etag.stsyn.ui.screen.book_out.book_out

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.tzh.retrofit_module.data.settings.AppConfiguration
import com.tzh.retrofit_module.domain.model.bookIn.BookInItem
import com.tzh.retrofit_module.domain.model.bookOut.BookOutResponse
import com.tzh.retrofit_module.domain.repository.BookOutRepository
import com.tzh.retrofit_module.util.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookOutViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler,
    private val bookOutRepository: BookOutRepository,
    private val appConfiguration: AppConfiguration
) : BaseViewModel(rfidHandler) {
    val TAG = "BookOut ViewModel"

    private val _bookOutUiState = MutableStateFlow(BookOutUiState())
    val bookOutUiState: StateFlow<BookOutUiState> = _bookOutUiState.asStateFlow()

    private val _getAllBookOutItemResponse =
        MutableStateFlow<ApiResponse<BookOutResponse>>(ApiResponse.Default)
    val getAllBookOutItemResponse: StateFlow<ApiResponse<BookOutResponse>> =
        _getAllBookOutItemResponse.asStateFlow()

    val settings = appConfiguration.appConfig

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
                    Log.d(
                        TAG,
                        "getAllBookOutItems: ${bookOutUiState.value.allBookOutItems.map { it.epc }}"
                    )
                }

                else -> {}
            }
        }
    }

    override fun onReceivedTagId(id: String) {
        addScannedItem(id)
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
        val allBookOutItems: List<BookInItem> = listOf(),
        val scannedItems: List<BookInItem> = listOf()
    )
}