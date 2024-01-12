package com.etag.stsyn.ui.screen.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.etag.stsyn.data.localStorage.LocalDataStore
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
    rfidHandler: ZebraRfidHandler,
    private val localDataStore: LocalDataStore,
    private val loginRepository: LoginRepository
) : BaseViewModel(rfidHandler) {
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    init {
        updateScanType(ScanType.Single)
        //getUserByRfidId()
    }

    fun updateLoginStatus(isSuccessful: Boolean) {
        _loginUiState.update { it.copy(isLoginSuccessful = isSuccessful) }
    }

    fun disableReader() {
        removeListener()
    }

    private fun getUserByRfidId(rfidId: String) {
        viewModelScope.launch {
            if (rfidId.isNotEmpty()) {
                val user = loginRepository.getUserByRfidId(rfidId)
                _loginUiState.update {
                    it.copy(
                        user = user
                    )
                }
                localDataStore.saveUser(user)
            }
        }
    }

    val savedUser = localDataStore.getUser

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

    fun logOut() {
        viewModelScope.launch {
            loginRepository.logOut()
        }
    }


    override fun onReceivedTagId(id: String) {
        Log.d("TAG", "onReceivedTagId: $id")
        getUserByRfidId(id)
    }

    data class LoginUiState(
        val isLoginSuccessful: Boolean = false,
        val errorMessage: String = "",
        val user: User? = User("", "", "1234", "")
    )
}