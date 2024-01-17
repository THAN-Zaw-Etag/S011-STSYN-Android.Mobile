package com.etag.stsyn.ui.screen.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.etag.stsyn.data.localStorage.LocalDataStore
import com.etag.stsyn.data.model.LocalUser
import com.etag.stsyn.util.toToken
import com.tzh.retrofit_module.data.model.login.LoginRequest
import com.tzh.retrofit_module.data.model.login.UpdatePasswordRequest
import com.tzh.retrofit_module.data.repository.UserRepository
import com.tzh.retrofit_module.domain.model.login.LoginResponse
import com.tzh.retrofit_module.domain.model.login.UpdatePasswordResponse
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
class LoginViewModel @Inject constructor(
    private val rfidHandler: ZebraRfidHandler,
    private val localDataStore: LocalDataStore,
    private val userRepository: UserRepository
) : BaseViewModel(rfidHandler, "LoginViewmodel") {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    private val _loginResponse = MutableStateFlow<ApiResponse<LoginResponse>>(ApiResponse.Default)
    val loginResponse: StateFlow<ApiResponse<LoginResponse>> = _loginResponse.asStateFlow()

    private val _updatePasswordResponse =
        MutableStateFlow<ApiResponse<UpdatePasswordResponse>>(ApiResponse.Default)
    val updatePasswordResponse: StateFlow<ApiResponse<UpdatePasswordResponse>> =
        _updatePasswordResponse.asStateFlow()

    val savedUser = localDataStore.getUser
    val isLoggedIn = localDataStore.isLoggedIn

    init {
        updateScanType(ScanType.Single)
    }

    fun updateLoginStatus(isSuccessful: Boolean) {
        _loginUiState.update { it.copy(isLoginSuccessful = isSuccessful) }
    }

    private fun getUserByRfidId(rfidId: String) {
        viewModelScope.launch {

        }
    }

    fun saveUserToLocalStorage(localUser: LocalUser) {
        viewModelScope.launch {
            localDataStore.saveUser(localUser)
            updateLoginStatus(true)
        }
    }

    fun increaseLoginAttempt() {
        var currentCount = _loginUiState.value.attemptCount
        _loginUiState.update {
            it.copy(
                attemptCount = it.attemptCount + 1
            )
        }
    }

    fun login(password: String) {
        viewModelScope.launch {
            _loginResponse.value = ApiResponse.Loading
            delay(1000) // set delay for loading

            _loginResponse.value = userRepository.login(
                LoginRequest(
                    id = "",
                    nric = "",
                    rfid = "0210000000012", //_loginUiState.value.rfidId
                    password = password,
                    isFromMobile = true
                )
            )
        }
    }

    fun updatePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            _updatePasswordResponse.value = ApiResponse.Loading
            delay(1000)

            savedUser.collect {
                _updatePasswordResponse.value = userRepository.updatePassword(
                    it.token.toToken(),
                    UpdatePasswordRequest(
                        oldPassword = oldPassword,
                        newPassword = newPassword,
                        userId = it.id ?: ""
                    )
                )
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            //loginRepository.logOut()
        }
    }

    override fun onReceivedTagId(id: String) {
        _loginUiState.update { it.copy(rfidId = id) }
        Log.d("TAG", "onReceivedTagId: $id")
        getUserByRfidId(id)
    }

    data class LoginUiState(
        val isLoginSuccessful: Boolean = false,
        var attemptCount: Int = 0,
        val rfidId: String = "",
        val errorMessage: String = "",
        val user: LocalUser? = LocalUser("", "", "1234", "")
    )
}