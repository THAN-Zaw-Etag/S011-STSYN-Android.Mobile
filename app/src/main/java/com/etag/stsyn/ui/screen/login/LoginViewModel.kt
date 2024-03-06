package com.etag.stsyn.ui.screen.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.model.LocalUser
import com.tzh.retrofit_module.data.model.login.LoginRequest
import com.tzh.retrofit_module.data.model.login.UpdatePasswordRequest
import com.tzh.retrofit_module.data.settings.AppConfigModel
import com.tzh.retrofit_module.data.settings.AppConfiguration
import com.tzh.retrofit_module.domain.model.login.LoginResponse
import com.tzh.retrofit_module.domain.model.login.MenuAccessRight
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.model.login.toLocalUser
import com.tzh.retrofit_module.domain.model.user.GetUserByEPCResponse
import com.tzh.retrofit_module.domain.model.user.UserMenuAccessRightsByIdResponse
import com.tzh.retrofit_module.domain.model.user.UserModel
import com.tzh.retrofit_module.domain.repository.UserRepository
import com.tzh.retrofit_module.util.ApiResponse
import com.tzh.retrofit_module.util.log.Logger
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
    private val userRepository: UserRepository,
    private val appConfiguration: AppConfiguration
) : BaseViewModel(rfidHandler) {

    companion object {
        const val TAG = "LoginViewModel"
    }

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _getUserResponse = MutableSharedFlow<ApiResponse<GetUserByEPCResponse>>()
    val getUserByEPCResponse: SharedFlow<ApiResponse<GetUserByEPCResponse>> =
        _getUserResponse.asSharedFlow()

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

    val appConfig = appConfiguration.appConfig

    private val _shouldShowEmptyBaseUrlDialog = MutableLiveData(false)
    val shouldShowEmptyBaseUrlDialog: LiveData<Boolean> = _shouldShowEmptyBaseUrlDialog

    private val _loading = MutableStateFlow(true)
    val loading = _loading.asStateFlow()


    private val _lockUserState =
        MutableStateFlow<ApiResponse<NormalResponse>>(ApiResponse.Default)
    val lockUserState: StateFlow<ApiResponse<NormalResponse>> =
        _lockUserState.asStateFlow()

    init {
        updateScanType(ScanType.Single)

        // if user is already logged in, fetch menu access rights from api
        viewModelScope.launch {
            launch {
                localDataStore.getUser.collect {
                    Logger.d("localUser: ${it.isLoggedIn}")
                    _savedUser.value = it
                    if (it.isLoggedIn) getUserMenuAccessRightsById()
                }
            }

            launch {
                localDataStore.getEpcUser.collect {
                    _epcModelUser.value = it
                }
            }

            launch { baseUrlStatus() }
            launch {
                delay(1000)
                _loading.value = false
            }

        }
    }

    fun updateAppConfig(appConfigModel: AppConfigModel) {
        viewModelScope.launch {
            appConfiguration.updateAppConfig(appConfigModel)
            baseUrlStatus()
        }
    }

    private fun baseUrlStatus() {
        viewModelScope.launch {
            appConfiguration.appConfig.collect { appConfigModel ->
                val baseUrl = appConfigModel.apiUrl
                if (baseUrl.isEmpty()) {
                    _shouldShowEmptyBaseUrlDialog.postValue(true)
                } else {
                    _shouldShowEmptyBaseUrlDialog.postValue(false)
                }
            }
        }
    }

    private fun updateLoginStatus(isSuccessful: Boolean) {
        _loginState.update { it.copy(isLoginSuccessful = isSuccessful) }
    }


    fun getUserByRfidId(rfidId: String) {
        viewModelScope.launch {

            // reset attempt count and errorMessage when user changes
            _loginState.update { it.copy(attemptCount = 0) }
            _loginResponse.value = ApiResponse.Default

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

    private fun saveUserToLocalStorage(localUser: LocalUser) {
        viewModelScope.launch {
            localDataStore.saveUser(localUser)
        }
    }

    private fun updateSODInitiateStatus(isSodInitiate: Boolean) {
        _loginState.update { it.copy(isSodInitiate = isSodInitiate) }
    }

    private fun updateShiftType(shift: Shift) {
        _loginState.update { it.copy(shift = shift) }
    }

    fun saveToken(token: String) {
        viewModelScope.launch { localDataStore.saveToken(token) }
    }

    private fun increaseLoginAttempt() {
        _loginState.update {
            it.copy(
                attemptCount = it.attemptCount + 1
            )
        }
    }

    fun login(passwordCharArray: CharArray) {
        shouldShowAuthorizationFailedDialog(false)
        viewModelScope.launch {
            _loginResponse.value = ApiResponse.Loading
            delay(1000) // set delay for loading
            val passwordString = String(passwordCharArray)

            _loginResponse.value = userRepository.login(
                LoginRequest(
                    id = "",
                    nric = "",
                    rfid = "455341303030303030303130", //TODO _loginUiState.value.rfid
                    password = passwordString,
                    isFromMobile = true
                )
            )

            handleLoginResponse()

            passwordCharArray.fill('0')
        }
    }

    private suspend fun handleLoginResponse() {
        when (loginResponse.value) {
            is ApiResponse.Success -> {
                updateLoginStatus(true)

                val data = (loginResponse.value as ApiResponse.Success<LoginResponse>).data
                saveUserToLocalStorage(data?.user.toLocalUser(data?.token))

                // update menu access rights
                _userMenuAccessRights.value = data?.rolePermission!!.handheldMenuAccessRight

                val checkStatus = data.checkStatus!!
                val isSodInitiate =
                    (checkStatus.isStart || checkStatus.isAdhoc) && checkStatus.isProgress
                updateShiftType(if (checkStatus.isStart) Shift.START else Shift.ADHOC)
                localDataStore.saveCheckStatusId(checkStatus.id.toString())
                updateSODInitiateStatus(isSodInitiate)
            }

            is ApiResponse.ApiError -> {
                increaseLoginAttempt()
                updateLoginStatus(false)
            }

            is ApiResponse.AuthorizationError -> {
                shouldShowAuthorizationFailedDialog(true)
            }

            else -> {}
        }
    }

    fun resetLoginResponseState() {
        _loginResponse.value = ApiResponse.Default
    }

    fun updatePassword(oldPasswordCharArr: CharArray, newPasswordChar: CharArray) {
        val oldPassword = String(oldPasswordCharArr)
        val newPassword = String(newPasswordChar)
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
            shouldShowAuthorizationFailedDialog(userMenuAccessRightsByIdResponse.value is ApiResponse.AuthorizationError)
            when (userMenuAccessRightsByIdResponse.value) {
                is ApiResponse.Success -> {
                    _userMenuAccessRights.value =
                        (userMenuAccessRightsByIdResponse.value as ApiResponse.Success).data?.rolePermission!!.handheldMenuAccessRight
                }

                is ApiResponse.AuthorizationError -> shouldShowAuthorizationFailedDialog(true)
                else -> {}
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            localDataStore.updateLoggedInStatus(false)
        }
    }

    fun lockUser(nric: String) {
        viewModelScope.launch {
            _lockUserState.value = ApiResponse.Loading
            delay(1000)
            _lockUserState.value = userRepository.lockUser(nric)
        }
    }

    fun navigateToScanScreen() {
        _loginState.update { it.copy(rfidId = "") }
    }

    override fun onReceivedTagId(id: String) {
        _loginState.update { it.copy(rfidId = id) }
        getUserByRfidId(id)
    }

    data class LoginState(
        val isLoginSuccessful: Boolean = false,
        var attemptCount: Int = 0,
        val rfidId: String = "",
        val isSodInitiate: Boolean = false,
        val shift: Shift = Shift.START,
        val errorMessage: String = "",
        val user: LocalUser? = LocalUser("", "", "1234", "")
    )
}

enum class Shift { START, ADHOC }