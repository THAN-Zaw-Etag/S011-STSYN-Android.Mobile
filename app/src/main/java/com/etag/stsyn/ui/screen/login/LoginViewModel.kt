package com.etag.stsyn.ui.screen.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.model.LocalUser
import com.tzh.retrofit_module.data.model.login.LoginRequest
import com.tzh.retrofit_module.data.model.login.UpdatePasswordRequest
import com.tzh.retrofit_module.data.network.BaseUrlValidator
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
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
        const val TEST_EPC = "2343424324234234221"
    }

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _validationErrorMessage = MutableStateFlow("")
    val validationErrorMessage = _validationErrorMessage.asStateFlow()

    private val _getUserResponse = MutableStateFlow<ApiResponse<GetUserByEPCResponse>>(ApiResponse.Default)
    val getUserByEPCResponse: StateFlow<ApiResponse<GetUserByEPCResponse>> = _getUserResponse.asStateFlow()

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

    private val _shouldShowEmptyBaseUrlDialog = MutableStateFlow(true)
    val shouldShowEmptyBaseUrlDialog: StateFlow<Boolean> = _shouldShowEmptyBaseUrlDialog.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val loading = _loading.asStateFlow()

    private val _lockUserState =
        MutableStateFlow<ApiResponse<NormalResponse>>(ApiResponse.Default)
    val lockUserState: StateFlow<ApiResponse<NormalResponse>> =
        _lockUserState.asStateFlow()

    private val _isSodInitiate = MutableStateFlow(false)
    val isSodInitiate = _isSodInitiate.asStateFlow()

    init {
        updateScanType(ScanType.Single)

        // if user is already logged in, fetch menu access rights from api
        viewModelScope.launch {
            launch {
                if (savedUser.value.isLoggedIn) getUserMenuAccessRightsById()
            }
            launch {
                localDataStore.getUser.collect {
                    Logger.d("localUser: ${it.isLoggedIn}")
                    _savedUser.value = it
                }
            }
            
            launch { 
                localDataStore.isSodInitiate.collect {
                    _isSodInitiate.value = it ?: false
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
    fun validateBaseUrl(url: String) {
        _validationErrorMessage.value = BaseUrlValidator.validate(url) {
            viewModelScope.launch {
                updateAppConfig(appConfig.first().copy(apiUrl = url))
                delay(500)
                _validationErrorMessage.value = "Validating..."
                delay(500)
                val response = userRepository.validateUrl()

                when (response){
                    is ApiResponse.Success -> {
                        _validationErrorMessage.value = ""
                        updateAppConfig(appConfig.last().copy(apiUrl = url))
                    }
                    is ApiResponse.ApiError -> {
                        _validationErrorMessage.value = "Invalid Error"
                        updateAppConfig(appConfig.last().copy(apiUrl = ""))
                    }
                    else -> {}
                }
            }
        }
    }

    private fun updateAppConfig(appConfigModel: AppConfigModel) {
        viewModelScope.launch {
            appConfiguration.updateAppConfig(appConfigModel)
            baseUrlStatus()
        }
    }

    private fun baseUrlStatus() {
        viewModelScope.launch {
            appConfiguration.appConfig.collect { appConfigModel ->
                if (appConfigModel.apiUrl.isEmpty()) {
                    _shouldShowEmptyBaseUrlDialog.value = true
                } else {
                    _shouldShowEmptyBaseUrlDialog.value = false
                }
            }
        }
    }

    private fun updateLoginStatus(isSuccessful: Boolean) {
        _loginState.update { it.copy(isLoginSuccessful = isSuccessful) }
    }


    fun getUserByRfidId(rfidId: String = "") {
        viewModelScope.launch {

            // reset attempt count and errorMessage when user changes
            _loginState.update { it.copy(attemptCount = 0) }
            _loginResponse.value = ApiResponse.Default

            _getUserResponse.value = ApiResponse.Loading
            val response = userRepository.getUserByEPC(TEST_EPC) //TODO change TEST_EPC to rfidId
            _getUserResponse.value = response
            observeGetUserByEpcResponse()
        }
    }

    private suspend fun observeGetUserByEpcResponse() {
        getUserByEPCResponse.collect {
            when (it) {
                is ApiResponse.Success -> {
                    if (it.data?.userModel != null) saveUserByEpcResponseToLocal(it.data!!.userModel)
                }
                else -> {}
            }
        }
    }

    private fun saveUserByEpcResponseToLocal(userModel: UserModel) {
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
                    rfid = TEST_EPC, //TODO _loginUiState.value.rfid
                    password = passwordString,
                    isFromMobile = true
                )
            )

            handleLoginResponse()
            passwordCharArray.fill('0')
        }
    }

    private fun handleLoginResponse() {
        viewModelScope.launch {
            loginResponse.collect {
                when (it) {
                    is ApiResponse.Success -> {
                        updateLoginStatus(true)

                        val data = it.data
                        saveUserToLocalStorage(data?.user.toLocalUser(data?.token))

                        // update menu access rights
                        _userMenuAccessRights.value = data?.rolePermission!!.handheldMenuAccessRight

                        val checkStatus = data.checkStatus!!
                        val isSodInitiate = (checkStatus.isStart || checkStatus.isAdhoc) && checkStatus.isProgress
                        updateShiftType(if (checkStatus.isStart) Shift.START else Shift.ADHOC)
                        updateSODInitiateStatus(isSodInitiate)

                        localDataStore.updateIsSodInitiateStatus(isSodInitiate)
                        localDataStore.saveCheckStatusId(checkStatus.id.toString())
                        localDataStore.saveLoggedInStatus(!isSodInitiate)
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