package com.etag.stsyn.ui.viewmodel

import android.util.Log
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.etag.stsyn.ui.navigation.Routes
import com.etag.stsyn.ui.theme.Purple80
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
    companion object {
        const val TAG = "SharedUiViewModel"
    }

    private val _uiState = MutableStateFlow(SharedUiState())
    val uiState: StateFlow<SharedUiState> = _uiState.asStateFlow()

    override fun onReceivedTagId(id: String) {
        Log.d(TAG, "onReceivedTagId: $id")
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

    fun updateAppBarNavigationIcon(icon: ImageVector) {
        _uiState.update { it.copy(icon = icon) }
    }

    fun updateTopBarTitle(title: String) {
        _uiState.update {
            it.copy(title = title)
        }
    }

    fun updateStatusBarColor(color: Color) {
        _uiState.update { it.copy(statusBarColor = color) }
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
    val statusBarColor: Color = Purple80,
    val icon: ImageVector = Icons.Default.Menu,
    val selectedBottomNavigationItem: String = Routes.HomeScreen.title,
    val showBottomNavigationBar: Boolean = false
)