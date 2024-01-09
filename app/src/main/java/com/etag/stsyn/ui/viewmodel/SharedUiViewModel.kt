package com.etag.stsyn.ui.viewmodel

import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.etag.stsyn.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SharedUiViewModel @Inject constructor(
    val rfidHandler: ZebraRfidHandler
) : BaseViewModel(rfidHandler) {
    private val _uiState = MutableStateFlow(SharedUiState())
    val uiState: StateFlow<SharedUiState> = _uiState.asStateFlow()

    override fun onReceivedTagId(id: String) {

    }

    fun updateTopAppBarStatus(show: Boolean) {
        _uiState.update {
            it.copy(showTopAppBar = show)
        }
    }

    fun updateBottomNavigationSelectedItem(title: String) {
        _uiState.update {
            it.copy(selectedBottomNavigationItem = title)
        }
    }

    fun updateTopBarTitle(title: String) {
        _uiState.update {
            it.copy(title = title)
        }
    }

    fun updateBottomNavigationBarStatus(showBottomNavigationBar: Boolean) {
        _uiState.update {
            it.copy(
                showBottomNavigationBar = showBottomNavigationBar
            )
        }
    }
}

data class SharedUiState(
    val title: String = Routes.HomeScreen.title,
    val showTopAppBar: Boolean = true,
    val selectedBottomNavigationItem: String = Routes.HomeScreen.title,
    val showBottomNavigationBar: Boolean = false
)