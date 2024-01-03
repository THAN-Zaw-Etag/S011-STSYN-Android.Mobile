package com.etag.stsyn.ui.screen.main

import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.etag.stsyn.data.model.User
import com.etag.stsyn.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler, private val loginRepository: LoginRepository
) : BaseViewModel(rfidHandler) {
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    fun updateLoginStatus(isSuccessful: Boolean) {
        _loginUiState.update { it.copy(isLoginSuccessful = isSuccessful) }
    }

    fun doneNavigate() {
        _loginUiState.update {
            it.copy(
                isLoginSuccessful = false, user = null, errorMessage = ""
            )
        }
    }

    private fun getUserByRfidId(rfidId: String) {
        viewModelScope.launch {
            val user = loginRepository.getUserByRfidId(rfidId)
            _loginUiState.update {
                it.copy(
                    isLoginSuccessful = false, user = user
                )
            }

        }
    }

    private fun updateLoginErrorMessage(error: String) {
        _loginUiState.update {
            it.copy(errorMessage = error)
        }
    }

    fun login(password: String) {
        viewModelScope.launch {
            // convert userResponse to user request model for login
            if (password.trim().isEmpty()) updateLoginErrorMessage("Password must not be empty!")
            else {
                val isLoginSuccessful = loginRepository.login(loginUiState.value.user!!, password)
                if (!isLoginSuccessful) updateLoginErrorMessage("Wrong password!")
                else updateLoginErrorMessage("")
                updateLoginStatus(isLoginSuccessful)
            }
        }
    }

    data class LoginUiState(
        val isLoginSuccessful: Boolean = false,
        val errorMessage: String = "",
        val user: User? = User("", "", "1234", "")
    )

    override fun onReceivedTagId(id: String) {
        getUserByRfidId(id)
        stopScan()
    }
}