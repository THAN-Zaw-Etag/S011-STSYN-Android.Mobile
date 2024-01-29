package com.etag.stsyn.ui.screen.book_in.book_in_box

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.core.reader.ZebraRfidHandler
import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.mapper.toItemMovementLogs
import com.tzh.retrofit_module.data.model.book_in.ItemMovementLog
import com.tzh.retrofit_module.data.model.book_in.PrintJob
import com.tzh.retrofit_module.data.model.book_in.SaveBookInRequest
import com.tzh.retrofit_module.data.settings.AppConfiguration
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem
import com.tzh.retrofit_module.domain.model.bookIn.GetAllBookInItemsOfBoxResponse
import com.tzh.retrofit_module.domain.model.bookIn.SelectBoxForBookInResponse
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.model.user.UserModel
import com.tzh.retrofit_module.domain.repository.BookInRepository
import com.tzh.retrofit_module.domain.repository.UserRepository
import com.tzh.retrofit_module.enum.ItemStatus
import com.tzh.retrofit_module.util.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookInBoxViewModel @Inject constructor(
    rfidHandler: ZebraRfidHandler,
    private val localDataStore: LocalDataStore,
    private val bookInRepository: BookInRepository,
    private val appConfiguration: AppConfiguration,
    private val userRepository: UserRepository
) : BaseViewModel(rfidHandler) {

    private val _boxItemsForBookInResponse =
        MutableStateFlow<ApiResponse<SelectBoxForBookInResponse>>(ApiResponse.Default)
    val boxItemsForBookInResponse: StateFlow<ApiResponse<SelectBoxForBookInResponse>> =
        _boxItemsForBookInResponse.asStateFlow()

    private val _getAllItemsOfBox =
        MutableStateFlow<ApiResponse<GetAllBookInItemsOfBoxResponse>>(ApiResponse.Default)
    val getAllItemsOfBox: StateFlow<ApiResponse<GetAllBookInItemsOfBoxResponse>> =
        _getAllItemsOfBox.asStateFlow()

    private val _saveBookInBoxResponse =
        MutableStateFlow<ApiResponse<NormalResponse>>(ApiResponse.Default)
    val saveBookInBoxResponse: StateFlow<ApiResponse<NormalResponse>> =
        _saveBookInBoxResponse.asStateFlow()

    private val _bookInBoxUiState = MutableStateFlow(BookInBoxUiState())
    val bookInBoxUiState: StateFlow<BookInBoxUiState> = _bookInBoxUiState.asStateFlow()

    private var boxItems = MutableStateFlow<List<BoxItem>>(emptyList())  // items from api
    val scannedItemsList = MutableStateFlow<List<String>>(emptyList()) // scanned tag list

    val user = localDataStore.getUser
    private val appConfig = appConfiguration.appConfig

    init {
        updateScanType(ScanType.Single)
        getAllBoxesForBookInItem()
    }

    private fun addScannedItemToList(id: String) {
        scannedItemsList.update { currentList ->
            if (id in currentList) {
                currentList
            } else {
                currentList + id
            }
        }
    }

    fun updateBookInBoxScanStatus(bookInBoxScanType: BookInBoxScanType) {
        _bookInBoxUiState.update { it.copy(bookInBoxScanType = bookInBoxScanType) }
    }

    fun saveBookInBox() {
        viewModelScope.launch {

            // set initial loading state
            _saveBookInBoxResponse.value = ApiResponse.Loading

            val readerId = appConfig.first().handheldReaderId
            val currentDate =
                "2024-01-29T03:29:20.016Z" //TODO replace with DateUtil.getCurrentDate()
            val buddy = bookInBoxUiState.value.issuerUser
            val scannedItems =
                _bookInBoxUiState.value.allItemsOfBox.filter { it.epc in scannedItemsList.value }
            val outStandingItems =
                _bookInBoxUiState.value.allItemsOfBox.filter { it.epc !in scannedItemsList.value }

            val itemMovementLogs = mutableListOf<ItemMovementLog>()
            val scannedBox = bookInBoxUiState.value.scannedBox
            if (!scannedItems.map { it.epc }.contains(scannedBox.epc)) {
                itemMovementLogs.add(
                    ItemMovementLog(
                        itemId = scannedBox.id.toInt(),
                        itemStatus = ItemStatus.BookIn.title,
                        workLoc = "",
                        issuerId = scannedBox.issuerId.toInt(),
                        date = currentDate,
                        handheldReaderId = readerId.toInt(),
                        receiverId = scannedBox.receiverId.toInt(),
                        approverId = 0,
                        remarks = if (bookInBoxUiState.value.isChecked) "Visual Check" else "",
                        receiverName = "",
                        calDate = scannedBox.calDate,
                        description = "",
                        buddyId = buddy?.userId ?: "0",
                        iS_ONSITE_TRANSFER = "0",
                        itemType = ""
                    )
                )
            }

            itemMovementLogs.addAll(
                scannedItems.toItemMovementLogs(
                    date = currentDate,
                    itemStatus = ItemStatus.BookIn,
                    buddyId = buddy?.userId,
                    isVisualChecked = _bookInBoxUiState.value.isChecked,
                    handleHeldId = readerId.toInt()
                )
            )
            itemMovementLogs.addAll(
                outStandingItems.toItemMovementLogs(
                    date = currentDate,
                    itemStatus = ItemStatus.OUTSTANDING,
                    buddyId = buddy?.userId,
                    isVisualChecked = _bookInBoxUiState.value.isChecked,
                    handleHeldId = readerId.toInt()
                )
            )
            val saveBookInBoxRequest = SaveBookInRequest(
                itemMovementLogs = itemMovementLogs, printJob = PrintJob(
                    date = currentDate,
                    handheldId = readerId.toInt(),
                    reportType = ItemStatus.BookIn.title,
                    userId = user.first().userId.toInt()
                )
            )

            _saveBookInBoxResponse.value = bookInRepository.saveBookIn(saveBookInBoxRequest)
        }
    }

    private fun checkUsCaseByBoxName(boxName: String) {
        viewModelScope.launch {
            when (val response = bookInRepository.checkUSCaseByBox(boxName)) {
                is ApiResponse.Success -> {
                    _bookInBoxUiState.update {
                        it.copy(
                            isUsCase = response.data?.isUsCase ?: false
                        )
                    }
                }

                is ApiResponse.AuthorizationError -> updateAuthorizationFailedDialogVisibility(true)
                else -> {}
            }
        }
    }

    fun getIssuerByEPC(epc: String) {
        viewModelScope.launch {
            when (val response = userRepository.getIssuerByEPC(epc)) {
                is ApiResponse.Success -> {
                    _bookInBoxUiState.update { it.copy(issuerUser = response.data?.userModel) }
                }

                else -> {}
            }
        }
    }

    override fun onReceivedTagId(id: String) {

        if (_boxItemsForBookInResponse.value is ApiResponse.Success) {
            Log.d("TAG", "onReceivedTagId: ${bookInBoxUiState.value.bookInBoxScanType} - $id")
            when (bookInBoxUiState.value.bookInBoxScanType) {
                BookInBoxScanType.BOX -> {
                    boxItems.value =
                        (_boxItemsForBookInResponse.value as ApiResponse.Success<SelectBoxForBookInResponse>).data!!.items
                    val scannedBoxItem = boxItems.value.find { it.epc == id }
                    if (scannedBoxItem != null) {
                        _bookInBoxUiState.update { it.copy(scannedBox = scannedBoxItem) }
                        getAllBookInItemsOfBox(
                            box = scannedBoxItem.box,
                            status = scannedBoxItem.itemStatus
                        )
                        // check box is us case
                        checkUsCaseByBoxName(scannedBoxItem.box)
                    }
                }

                BookInBoxScanType.ITEMS -> {
                    when (_getAllItemsOfBox.value) {
                        is ApiResponse.Success -> {
                            val boxesOfItem = _bookInBoxUiState.value.allItemsOfBox
                            val hasCurrentItemScanned = id in boxesOfItem.map { it.epc }
                            if (hasCurrentItemScanned) addScannedItemToList(id)
                        }

                        else -> {}
                    }
                }

                BookInBoxScanType.BUDDY -> {
                    getIssuerByEPC(id)
                }
            }

        }
    }

    fun refreshScannedBox() {
        viewModelScope.launch {
            boxItems.update { emptyList() }
            scannedItemsList.update { emptyList() }
            _bookInBoxUiState.update {
                it.copy(scannedBox = BoxItem(), allItemsOfBox = mutableListOf())
            }
        }
    }

    fun toggleVisualCheck(enable: Boolean) {
        _bookInBoxUiState.update { it.copy(isChecked = enable) }
        if (enable) {
            scannedItemsList.update { _bookInBoxUiState.value.allItemsOfBox.map { it.epc } }
        } else scannedItemsList.update { emptyList() }
    }

    fun resetScannedItems() {
        scannedItemsList.update { emptyList() }
    }

    private fun getAllBoxesForBookInItem() {
        viewModelScope.launch {
            _boxItemsForBookInResponse.value = ApiResponse.Loading
            delay(1000)
            user.collect {
                _boxItemsForBookInResponse.value = bookInRepository.getBoxItemsForBookIn(it.userId)

                when (_boxItemsForBookInResponse.value) {
                    is ApiResponse.Success -> {
                        val boxes =
                            (_boxItemsForBookInResponse.value as ApiResponse.Success<SelectBoxForBookInResponse>).data?.items
                                ?: emptyList()
                        _bookInBoxUiState.update { it.copy(allBoxes = boxes) }
                    }

                    is ApiResponse.AuthorizationError -> updateAuthorizationFailedDialogVisibility(
                        true
                    )

                    else -> {}
                }
            }
        }
    }

    private fun getAllBookInItemsOfBox(
        box: String,
        status: String,
    ) {
        viewModelScope.launch {
            user.collect {
                _getAllItemsOfBox.value = ApiResponse.Loading
                _getAllItemsOfBox.value = bookInRepository.getAllBookItemsOfBox(
                    box = box, status = status, loginUserId = it.userId
                )

                when (_getAllItemsOfBox.value) {
                    is ApiResponse.Success -> {
                        val allItems = (_getAllItemsOfBox.value as ApiResponse.Success).data!!.items
                        viewModelScope.launch {
                            _getAllItemsOfBox.collect { response ->
                                _bookInBoxUiState.update { it.copy(allItemsOfBox = allItems.toMutableList()) }
                            }
                        }
                    }

                    is ApiResponse.AuthorizationError -> {
                        updateAuthorizationFailedDialogVisibility(true)
                    }

                    else -> {}
                }
            }
        }
    }

    enum class BookInBoxScanType {
        BOX, ITEMS, BUDDY
    }

    data class BookInBoxUiState(
        val scannedBox: BoxItem = BoxItem(),
        val issuerUser: UserModel? = null,
        val isUsCase: Boolean = false,
        val isChecked: Boolean = false,
        val bookInBoxScanType: BookInBoxScanType = BookInBoxScanType.BOX,
        val allBoxes: List<BoxItem> = listOf(),
        val allItemsOfBox: MutableList<BoxItem> = mutableListOf()
    )
}