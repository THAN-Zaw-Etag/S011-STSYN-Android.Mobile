package com.etag.stsyn.ui.screen.login

import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.model.LocalUser
import com.tzh.retrofit_module.data.model.login.LoginRequest
import com.tzh.retrofit_module.data.model.login.UpdatePasswordRequest
import com.tzh.retrofit_module.domain.model.login.LoginResponse
import com.tzh.retrofit_module.domain.model.login.MenuAccessRight
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.model.user.GetUserByEPCResponse
import com.tzh.retrofit_module.domain.model.user.UserMenuAccessRightsByIdResponse
import com.tzh.retrofit_module.domain.model.user.UserModel
import com.tzh.retrofit_module.domain.repository.UserRepository
import com.tzh.retrofit_module.util.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val rfidHandler: ZebraRfidHandler,
    private val localDataStore: LocalDataStore,
    private val userRepository: UserRepository
) : BaseViewModel(rfidHandler) {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    private val _getUserResponse = MutableSharedFlow<ApiResponse<GetUserByEPCResponse>>()
    val getUserByEPCResponse: SharedFlow<ApiResponse<GetUserByEPCResponse>> = _getUserResponse.asSharedFlow()

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

    private val _savedUser = MutableStateFlow(LocalUser())
    val savedUser: StateFlow<LocalUser> = _savedUser.asStateFlow()

    private val _epcModelUser = MutableStateFlow(UserModel())
    val epcModelUser: StateFlow<UserModel> = _epcModelUser.asStateFlow()

    init {
        updateScanType(ScanType.Single)
        // if user is already logged in, fetch menu access rights from api
        viewModelScope.launch {
            launch {
                localDataStore.getUser.collect {
                    _savedUser.value = it
                    if (it.isLoggedIn) getUserMenuAccessRightsById()
                }
            }

            launch {
                localDataStore.getEpcUser.collect {
                    _epcModelUser.value =it
                }
            }
        }
    }

    fun updateLoginStatus(isSuccessful: Boolean) {
        _loginUiState.update { it.copy(isLoginSuccessful = isSuccessful) }
    }


    private fun getUserByRfidId(rfidId: String) {
        viewModelScope.launch {
            _getUserResponse.emit(ApiResponse.Loading)
            val response = userRepository.getUserByEPC(rfidId)
            _getUserResponse.emit(response)
        }
    }

    fun saveUserByEpcResponseToLocal(userModel: UserModel) {
        viewModelScope.launch {
            localDataStore.saveEpcUser(userModel)
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

    fun login(passwordCharArray: CharArray) {
        updateAuthorizationFailedDialogVisibility(false)
        viewModelScope.launch {
            _loginResponse.value = ApiResponse.Loading
            delay(1000) // set delay for loading
            //TODO remove unused lines of code and encrypt password if required
            val passwordString = String(passwordCharArray)
            _loginResponse.value = userRepository.login(
                LoginRequest(
                    id = "",
                    nric = "",
                    rfid ="455341303030303030303130" , // _loginUiState.value.rfidId
                    password = passwordString,
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
            passwordCharArray.fill('0')
        }
    }
    fun updatePassword(oldPasswordCharArr: CharArray, newPasswordChar: CharArray) {
        val oldPassword =String(oldPasswordCharArr)
        val newPassword = String(newPasswordChar)
        //TODO remove unused lines of code and encrypt password if required
        updateAuthorizationFailedDialogVisibility(false)
        viewModelScope.launch {
            _updatePasswordResponse.value = ApiResponse.Loading
            delay(1000)
            savedUser.collect {
                _updatePasswordResponse.value = userRepository.updatePassword(
                    UpdatePasswordRequest(
                        oldPassword = oldPassword,
                        newPassword = newPassword,
                        userId = it.userId
                    )
                )

                // show authorization error dialog if error is authorization failed error
                updateAuthorizationFailedDialogVisibility(_updatePasswordResponse.value is ApiResponse.AuthorizationError)
            }
        }
        oldPasswordCharArr.fill('0')
        newPasswordChar.fill('0')
    }

    private fun getUserMenuAccessRightsById() {
        viewModelScope.launch {
            userMenuAccessRightsByIdResponse.value = ApiResponse.Loading
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

    fun navigateToScanScreen() {
        _loginUiState.update { it.copy(rfidId = "") }
    }

    override fun onReceivedTagId(id: String) {
        _loginUiState.update { it.copy(rfidId = id) }
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