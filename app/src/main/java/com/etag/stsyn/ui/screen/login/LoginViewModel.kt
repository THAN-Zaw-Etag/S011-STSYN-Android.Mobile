package com.etag.stsyn.ui.screen.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.model.LocalUser
import com.tzh.retrofit_module.data.model.login.LoginRequest
import com.tzh.retrofit_module.data.model.login.UpdatePasswordRequest
import com.tzh.retrofit_module.domain.model.bookIn.RefreshTokenResponse
import com.tzh.retrofit_module.domain.model.login.LoginResponse
import com.tzh.retrofit_module.domain.model.login.MenuAccessRight
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.model.user.UserMenuAccessRightsByIdResponse
import com.tzh.retrofit_module.domain.repository.UserRepository
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
) : BaseViewModel(rfidHandler) {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    private val _loginResponse = MutableStateFlow<ApiResponse<LoginResponse>>(ApiResponse.Default)
    val loginResponse: StateFlow<ApiResponse<LoginResponse>> = _loginResponse.asStateFlow()

    private val _updatePasswordResponse =
        MutableStateFlow<ApiResponse<NormalResponse>>(ApiResponse.Default)
    val updatePasswordResponse: StateFlow<ApiResponse<NormalResponse>> =
        _updatePasswordResponse.asStateFlow()

    private val userMenuAccessRightsByIdResponse =
        MutableStateFlow<ApiResponse<UserMenuAccessRightsByIdResponse>>(ApiResponse.Default)

    private val _userMenuAccessRights = MutableStateFlow(MenuAccessRight())
    val userMenuAccessRight: StateFlow<MenuAccessRight> = _userMenuAccessRights.asStateFlow()

    private val _refreshTokenResponse =
        MutableStateFlow<ApiResponse<RefreshTokenResponse>>(ApiResponse.Default)
    val refreshTokenResponse: StateFlow<ApiResponse<RefreshTokenResponse>> =
        _refreshTokenResponse.asStateFlow()

    private val _savedUser = MutableStateFlow(LocalUser())
    val savedUser: StateFlow<LocalUser> = _savedUser.asStateFlow()

    init {
        updateScanType(ScanType.Single)

        // if user is already logged in, fetch menu access rights from api
        viewModelScope.launch {
            localDataStore.getUser.collect {
                _savedUser.value = it
                println("isLoggedIn: ${it.token}")
                if (it.isLoggedIn) getUserMenuAccessRightsById()
            }
        }
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
        //var currentCount = _loginUiState.value.attemptCount
        _loginUiState.update {
            it.copy(
                attemptCount = it.attemptCount + 1
            )
        }
    }

    fun login(password: String) {
        updateAuthorizationFailedDialogVisibility(false)
        viewModelScope.launch {
            _loginResponse.value = ApiResponse.Loading
            delay(1000) // set delay for loading

            _loginResponse.value = userRepository.login(
                LoginRequest(
                    id = "",
                    nric = "",
                    rfid = _loginUiState.value.rfidId, //_loginUiState.value.rfidId
                    password = password,
                    isFromMobile = true
                )
            )

            // when login is successful, update user menu access rights
            when (_loginResponse.value) {
                is ApiResponse.Success -> {
                    updateLoginStatus(true)
                    _userMenuAccessRights.value =
                        (_loginResponse.value as ApiResponse.Success<LoginResponse>).data?.rolePermission!!.handheldMenuAccessRight
                }

                is ApiResponse.AuthorizationError -> {
                    updateAuthorizationFailedDialogVisibility(true)
                }

                else -> {}
            }
        }
    }

    fun refreshToken() {
        viewModelScope.launch {
            _refreshTokenResponse.value = userRepository.refreshToken()
        }
    }

    fun updatePassword(oldPassword: String, newPassword: String) {
        updateAuthorizationFailedDialogVisibility(false)
        viewModelScope.launch {
            _updatePasswordResponse.value = ApiResponse.Loading
            delay(1000)

            savedUser.collect {
                _updatePasswordResponse.value = userRepository.updatePassword(
                    UpdatePasswordRequest(
                        oldPassword = oldPassword,
                        newPassword = newPassword,
                        userId = it.userId ?: ""
                    )
                )

                // show authorization error dialog if error is authorization failed error
                updateAuthorizationFailedDialogVisibility(_updatePasswordResponse.value is ApiResponse.AuthorizationError)
            }
        }
    }

    private fun getUserMenuAccessRightsById() {
        //updateAuthorizationFailedDialogVisibility(false)
        viewModelScope.launch {
            userMenuAccessRightsByIdResponse.value = ApiResponse.Loading
            println("userMenuAccessRightsByIdResponse: ${_userMenuAccessRights.value}")
            delay(1000)
            userMenuAccessRightsByIdResponse.value = userRepository.getUserMenuAccessRightsById()

            // when data is fetched, update user menu access rights
            when (userMenuAccessRightsByIdResponse.value) {
                is ApiResponse.Success -> {
                    _userMenuAccessRights.value =
                        (userMenuAccessRightsByIdResponse.value as ApiResponse.Success).data?.rolePermission!!.handheldMenuAccessRight
                }

                is ApiResponse.AuthorizationError -> updateAuthorizationFailedDialogVisibility(true)
                else -> {}
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            localDataStore.updateLoggedInStatus(false)
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