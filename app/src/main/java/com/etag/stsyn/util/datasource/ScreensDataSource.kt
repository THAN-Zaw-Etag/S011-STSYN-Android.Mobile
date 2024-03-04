package com.etag.stsyn.util.datasource

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.etag.stsyn.R
import com.etag.stsyn.core.BaseViewModel
import com.etag.stsyn.enums.OptionType
import com.etag.stsyn.ui.components.CustomIcon
import com.etag.stsyn.ui.screen.acct_check.AccountCheckViewModel
import com.etag.stsyn.ui.screen.acct_check.AcctCheckCountScreen
import com.etag.stsyn.ui.screen.acct_check.AcctCheckSaveScreen
import com.etag.stsyn.ui.screen.acct_check.AcctCheckScanScreen
import com.etag.stsyn.ui.screen.book_in.book_in.BookInCountScreen
import com.etag.stsyn.ui.screen.book_in.book_in.BookInSaveScreen
import com.etag.stsyn.ui.screen.book_in.book_in.BookInScanScreen
import com.etag.stsyn.ui.screen.book_in.book_in.BookInViewModel
import com.etag.stsyn.ui.screen.book_in.book_in_box.BookInBoxCountScreen
import com.etag.stsyn.ui.screen.book_in.book_in_box.BookInBoxSaveScreen
import com.etag.stsyn.ui.screen.book_in.book_in_box.BookInBoxScanScreen
import com.etag.stsyn.ui.screen.book_in.book_in_box.BookInBoxViewModel
import com.etag.stsyn.ui.screen.book_in.book_in_cal.BookInCalCountScreen
import com.etag.stsyn.ui.screen.book_in.book_in_cal.BookInCalSaveScreen
import com.etag.stsyn.ui.screen.book_in.book_in_cal.BookInCalScanScreen
import com.etag.stsyn.ui.screen.book_in.book_in_cal.BookInCalViewModel
import com.etag.stsyn.ui.screen.book_in.det_p_loan.DetPLoanCountScreen
import com.etag.stsyn.ui.screen.book_in.det_p_loan.DetPLoanSaveScreen
import com.etag.stsyn.ui.screen.book_in.det_p_loan.DetPLoanScanScreen
import com.etag.stsyn.ui.screen.book_in.det_p_loan.DetPLoanViewModel
import com.etag.stsyn.ui.screen.book_in.det_p_loan_box.DetPLoanBoxCountScreen
import com.etag.stsyn.ui.screen.book_in.det_p_loan_box.DetPLoanBoxSaveScreen
import com.etag.stsyn.ui.screen.book_in.det_p_loan_box.DetPLoanBoxScanScreen
import com.etag.stsyn.ui.screen.book_in.det_p_loan_box.DetPLoanBoxViewModel
import com.etag.stsyn.ui.screen.book_in.t_loan.TLoanCountScreen
import com.etag.stsyn.ui.screen.book_in.t_loan.TLoanSaveScreen
import com.etag.stsyn.ui.screen.book_in.t_loan.TLoanScanScreen
import com.etag.stsyn.ui.screen.book_in.t_loan.TLoanViewModel
import com.etag.stsyn.ui.screen.book_in.t_loan_box.TLoanBoxCountScreen
import com.etag.stsyn.ui.screen.book_in.t_loan_box.TLoanBoxSaveScreen
import com.etag.stsyn.ui.screen.book_in.t_loan_box.TLoanBoxScanScreen
import com.etag.stsyn.ui.screen.book_in.t_loan_box.TLoanBoxViewModel
import com.etag.stsyn.ui.screen.book_out.book_out.BookOutSaveScreen
import com.etag.stsyn.ui.screen.book_out.book_out.BookOutScanScreen
import com.etag.stsyn.ui.screen.book_out.book_out.BookOutViewModel
import com.etag.stsyn.ui.screen.book_out.book_out_box.BookOutBoxCountScreen
import com.etag.stsyn.ui.screen.book_out.book_out_box.BookOutBoxSaveScreen
import com.etag.stsyn.ui.screen.book_out.book_out_box.BookOutBoxScanScreen
import com.etag.stsyn.ui.screen.book_out.book_out_box.BookOutBoxViewModel
import com.etag.stsyn.ui.screen.other_operations.det_p_loan.OtherDetPLoanOutSaveScreen
import com.etag.stsyn.ui.screen.other_operations.det_p_loan.OtherDetPLoanScanScreen
import com.etag.stsyn.ui.screen.other_operations.det_p_loan.OtherDetPLoanViewModel
import com.etag.stsyn.ui.screen.other_operations.det_p_loan_box.OtherDetPLoanBoxViewModel
import com.etag.stsyn.ui.screen.other_operations.det_p_loan_box.OtherDetPLoanOutBoxCountScreen
import com.etag.stsyn.ui.screen.other_operations.det_p_loan_box.OtherDetPLoanOutBoxSaveScreen
import com.etag.stsyn.ui.screen.other_operations.det_p_loan_box.OtherDetPLoanOutBoxScanScreen
import com.etag.stsyn.ui.screen.other_operations.onsite_check_in_out.CheckInOutSaveScreen
import com.etag.stsyn.ui.screen.other_operations.onsite_check_in_out.CheckInOutScreen
import com.etag.stsyn.ui.screen.other_operations.onsite_check_in_out.OnsiteCheckInOutViewModel
import com.etag.stsyn.ui.screen.other_operations.onsite_verification.OnsiteVerificationCountScreen
import com.etag.stsyn.ui.screen.other_operations.onsite_verification.OnsiteVerificationSaveScreen
import com.etag.stsyn.ui.screen.other_operations.onsite_verification.OnsiteVerificationViewModel
import com.etag.stsyn.ui.screen.other_operations.onsite_verification.OnsiteVerificationScreen
import com.etag.stsyn.ui.screen.other_operations.t_loan_out.OtherTLoanOutSaveScreen
import com.etag.stsyn.ui.screen.other_operations.t_loan_out.OtherTLoanOutScanScreen
import com.etag.stsyn.ui.screen.other_operations.t_loan_out.OtherTLoanOutViewModel
import com.etag.stsyn.ui.screen.other_operations.t_loan_out_box.OtherTLoanOutBoxCountScreen
import com.etag.stsyn.ui.screen.other_operations.t_loan_out_box.OtherTLoanOutBoxSaveScreen
import com.etag.stsyn.ui.screen.other_operations.t_loan_out_box.OtherTLoanOutBoxScanScreen
import com.etag.stsyn.ui.screen.other_operations.t_loan_out_box.OtherTLoanOutBoxViewModel

data class TabScreen(
    val title: String,
    val icon: CustomIcon,
    val screen: (@Composable () -> Unit)? = null
)

@Composable
fun getViewModelByOptionType(optionType: OptionType): BaseViewModel {
    return when (optionType) {
        OptionType.BookIn -> hiltViewModel<BookInViewModel>()
        OptionType.BookInBox -> hiltViewModel<BookInBoxViewModel>()
        OptionType.BookInCalibration -> hiltViewModel<BookInCalViewModel>()
        OptionType.BookInTLoan -> hiltViewModel<TLoanViewModel>()
        OptionType.BookInTLoanBox -> hiltViewModel<TLoanBoxViewModel>()
        OptionType.BookInDetPLoan -> hiltViewModel<DetPLoanViewModel>()
        OptionType.BookInDetPLoanBox -> hiltViewModel<DetPLoanBoxViewModel>()

        OptionType.BookOut -> hiltViewModel<BookOutViewModel>()
        OptionType.BookOutBox -> hiltViewModel<BookOutBoxViewModel>()

        OptionType.AccountCheck -> hiltViewModel<AccountCheckViewModel>()

        OptionType.OnsiteCheckInOut -> hiltViewModel<OnsiteCheckInOutViewModel>()
        OptionType.OnsiteVerification -> hiltViewModel<OnsiteVerificationViewModel>()
        OptionType.OtherTLoan -> hiltViewModel<OtherTLoanOutViewModel>()
        OptionType.OtherTLoanBox -> hiltViewModel<OtherTLoanOutBoxViewModel>()
        OptionType.OtherDetPLoan -> hiltViewModel<OtherDetPLoanViewModel>()
        OptionType.OtherDetPLoanBox -> hiltViewModel<OtherDetPLoanBoxViewModel>()
    }
}

object ScreensDataSource {
    fun bookOutScreens(viewModel: BookOutViewModel) = listOf(
        TabScreen(
            title = "Book out",
            icon = CustomIcon.Resource(R.drawable.book_out_tab_icon),
            screen = { BookOutScanScreen(viewModel) }),
        TabScreen(
            title = "Save",
            icon = CustomIcon.Vector(Icons.Default.Save),
            screen = { BookOutSaveScreen(viewModel) }),
        TabScreen(title = "Exit", icon = CustomIcon.Vector(Icons.AutoMirrored.Filled.ExitToApp)),
    )

    fun bookOutBoxScreens(viewModel: BookOutBoxViewModel) = listOf(
        TabScreen(
            title = "Book out box",
            icon = CustomIcon.Resource(R.drawable.book_out_tab_icon),
            screen = { BookOutBoxScanScreen(viewModel) }),
        TabScreen(
            title = "Count",
            icon = CustomIcon.Resource(R.drawable.tally),
            screen = { BookOutBoxCountScreen(viewModel) }),
        TabScreen(
            title = "Save",
            icon = CustomIcon.Vector(Icons.Default.Save),
            screen = { BookOutBoxSaveScreen(viewModel) }),
        TabScreen(title = "Exit", icon = CustomIcon.Vector(Icons.AutoMirrored.Filled.ExitToApp)),
    )

    fun bookInScreens(viewModel: BookInViewModel) = listOf(
        TabScreen(
            title = "Book in",
            icon = CustomIcon.Resource(R.drawable.book_in_tab_icon),
            screen = { BookInScanScreen(viewModel) }),
        TabScreen(
            title = "Count",
            icon = CustomIcon.Resource(R.drawable.tally),
            screen = { BookInCountScreen(viewModel) }),
        TabScreen(
            title = "Save",
            icon = CustomIcon.Vector(Icons.Default.Save),
            screen = { BookInSaveScreen(viewModel) }),
        TabScreen(title = "Exit", icon = CustomIcon.Vector(Icons.AutoMirrored.Filled.ExitToApp)),
    )

    fun bookInBoxScreens(viewModel: BookInBoxViewModel) = listOf(
        TabScreen(
            title = "Book in box",
            icon = CustomIcon.Resource(R.drawable.book_in_tab_icon),
            screen = { BookInBoxScanScreen(viewModel) }),
        TabScreen(
            title = "Count",
            icon = CustomIcon.Resource(R.drawable.tally),
            screen = { BookInBoxCountScreen(viewModel) }),
        TabScreen(
            title = "Save",
            icon = CustomIcon.Vector(Icons.Default.Save),
            screen = { BookInBoxSaveScreen(viewModel) }),
        TabScreen(title = "Exit", icon = CustomIcon.Vector(Icons.AutoMirrored.Filled.ExitToApp)),
    )

    fun bookInCalScreens(viewModel: BookInCalViewModel) = listOf(
        TabScreen(
            title = "Book in (cal)",
            icon = CustomIcon.Resource(R.drawable.book_in_tab_icon),
            screen = { BookInCalScanScreen(viewModel) }),
        TabScreen(
            title = "Count",
            icon = CustomIcon.Resource(R.drawable.tally),
            screen = { BookInCalCountScreen(viewModel) }),
        TabScreen(
            title = "Save",
            icon = CustomIcon.Vector(Icons.Default.Save),
            screen = { BookInCalSaveScreen(viewModel) }),
        TabScreen(title = "Exit", icon = CustomIcon.Vector(Icons.AutoMirrored.Filled.ExitToApp)),
    )

    fun bookInTLoanScreens(viewModel: TLoanViewModel) = listOf(
        TabScreen(
            title = "Book in (T-loan)",
            icon = CustomIcon.Resource(R.drawable.book_in_tab_icon),
            screen = { TLoanScanScreen(viewModel) }),
        TabScreen(
            title = "Count",
            icon = CustomIcon.Resource(R.drawable.tally),
            screen = { TLoanCountScreen(viewModel) }),
        TabScreen(
            title = "Save",
            icon = CustomIcon.Vector(Icons.Default.Save),
            screen = { TLoanSaveScreen(viewModel) }),
        TabScreen(title = "Exit", icon = CustomIcon.Vector(Icons.AutoMirrored.Filled.ExitToApp)),
    )

    fun bookInTLoanBoxScreens(viewModel: TLoanBoxViewModel) = listOf(
        TabScreen(
            title = "in (T-loan box)",
            icon = CustomIcon.Resource(R.drawable.book_in_tab_icon),
            screen = { TLoanBoxScanScreen(viewModel) }),
        TabScreen(
            title = "Count",
            icon = CustomIcon.Resource(R.drawable.tally),
            screen = { TLoanBoxCountScreen(viewModel) }),
        TabScreen(
            title = "Save",
            icon = CustomIcon.Vector(Icons.Default.Save),
            screen = { TLoanBoxSaveScreen(viewModel) }),
        TabScreen(title = "Exit", icon = CustomIcon.Vector(Icons.AutoMirrored.Filled.ExitToApp)),
    )

    fun bookInDetPLoanScreens(viewModel: DetPLoanViewModel) = listOf(
        TabScreen(
            title = "in (det/p-loan)",
            icon = CustomIcon.Resource(R.drawable.book_in_tab_icon),
            screen = { DetPLoanScanScreen(viewModel) }),
        TabScreen(
            title = "Count",
            icon = CustomIcon.Resource(R.drawable.tally),
            screen = { DetPLoanCountScreen(viewModel) }),
        TabScreen(
            title = "Save",
            icon = CustomIcon.Vector(Icons.Default.Save),
            screen = { DetPLoanSaveScreen(viewModel) }),
        TabScreen(title = "Exit", icon = CustomIcon.Vector(Icons.AutoMirrored.Filled.ExitToApp)),
    )

    fun bookInDetPLoanBoxScreens(viewModel: DetPLoanBoxViewModel) = listOf(
        TabScreen(
            title = "det/p-loan box",
            icon = CustomIcon.Resource(R.drawable.book_in_tab_icon),
            screen = { DetPLoanBoxScanScreen(viewModel) }),
        TabScreen(
            title = "Count",
            icon = CustomIcon.Resource(R.drawable.tally),
            screen = { DetPLoanBoxCountScreen(viewModel) }),
        TabScreen(
            title = "Save",
            icon = CustomIcon.Vector(Icons.Default.Save),
            screen = { DetPLoanBoxSaveScreen(viewModel) }),
        TabScreen(title = "Exit", icon = CustomIcon.Vector(Icons.AutoMirrored.Filled.ExitToApp)),
    )

    fun accountCheckScreens(viewModel: AccountCheckViewModel) = listOf(
        TabScreen(
            title = "acct check",
            icon = CustomIcon.Vector(Icons.Default.ArrowCircleUp),
            screen = { AcctCheckScanScreen(viewModel) }),
        TabScreen(
            title = "Count",
            icon = CustomIcon.Resource(R.drawable.tally),
            screen = { AcctCheckCountScreen(viewModel) }),
        TabScreen(
            title = "Save",
            icon = CustomIcon.Vector(Icons.Default.Save),
            screen = { AcctCheckSaveScreen(viewModel) }),
        TabScreen(title = "Exit", icon = CustomIcon.Vector(Icons.AutoMirrored.Filled.ExitToApp)),
    )

    fun onsiteCheckInOutScreens(viewModel: OnsiteCheckInOutViewModel) = listOf(
        TabScreen(
            title = "check in/out",
            icon = CustomIcon.Vector(Icons.Default.ArrowCircleUp),
            screen = { CheckInOutScreen(viewModel) }),
        TabScreen(
            title = "Save",
            icon = CustomIcon.Vector(Icons.Default.Save),
            screen = { CheckInOutSaveScreen(viewModel) }),
        TabScreen(title = "Exit", icon = CustomIcon.Vector(Icons.AutoMirrored.Filled.ExitToApp)),
    )

    fun onsiteVerificationScreens(viewModel: OnsiteVerificationViewModel) = listOf(
        TabScreen(
            title = "onsite verify",
            icon = CustomIcon.Vector(Icons.Default.ArrowCircleUp),
            screen = { OnsiteVerificationScreen(viewModel) }),
        TabScreen(
            title = "Count",
            icon = CustomIcon.Resource(R.drawable.tally),
            screen = { OnsiteVerificationCountScreen(viewModel) }),
        TabScreen(
            title = "Save",
            icon = CustomIcon.Vector(Icons.Default.Save),
            screen = { OnsiteVerificationSaveScreen(viewModel) }),
        TabScreen(title = "Exit", icon = CustomIcon.Vector(Icons.AutoMirrored.Filled.ExitToApp)),
    )

    fun onsiteTLoanScreens(viewModel: OtherTLoanOutViewModel) = listOf(
        TabScreen(
            title = "t-loan out",
            icon = CustomIcon.Vector(Icons.Default.ArrowCircleUp),
            screen = { OtherTLoanOutScanScreen(viewModel) }),
        TabScreen(
            title = "Save",
            icon = CustomIcon.Vector(Icons.Default.Save),
            screen = { OtherTLoanOutSaveScreen(viewModel) }),
        TabScreen(title = "Exit", icon = CustomIcon.Vector(Icons.AutoMirrored.Filled.ExitToApp)),
    )

    fun onsiteTLoanBoxScreens(viewModel: OtherTLoanOutBoxViewModel) = listOf(
        TabScreen(
            title = "t-loan box",
            icon = CustomIcon.Vector(Icons.Default.ArrowCircleUp),
            screen = { OtherTLoanOutBoxScanScreen(viewModel) }),
        TabScreen(
            title = "Count",
            icon = CustomIcon.Resource(R.drawable.tally),
            screen = { OtherTLoanOutBoxCountScreen(viewModel) }),
        TabScreen(
            title = "Save",
            icon = CustomIcon.Vector(Icons.Default.Save),
            screen = { OtherTLoanOutBoxSaveScreen(viewModel) }),
        TabScreen(title = "Exit", icon = CustomIcon.Vector(Icons.AutoMirrored.Filled.ExitToApp)),
    )

    fun onsiteDetPScreens(viewModel: OtherDetPLoanViewModel) = listOf(
        TabScreen(
            title = "det/p-loan",
            icon = CustomIcon.Vector(Icons.Default.ArrowCircleUp),
            screen = { OtherDetPLoanScanScreen(viewModel) }),
        TabScreen(
            title = "Save",
            icon = CustomIcon.Vector(Icons.Default.Save),
            screen = { OtherDetPLoanOutSaveScreen(viewModel) }),
        TabScreen(title = "Exit", icon = CustomIcon.Vector(Icons.AutoMirrored.Filled.ExitToApp)),
    )

    fun onsiteDetPLoanBoxScreens(viewModel: OtherDetPLoanBoxViewModel) = listOf(
        TabScreen(
            title = "det/p-loan box",
            icon = CustomIcon.Vector(Icons.Default.ArrowCircleUp),
            screen = { OtherDetPLoanOutBoxScanScreen(viewModel) }),
        TabScreen(
            title = "Count",
            icon = CustomIcon.Resource(R.drawable.tally),
            screen = { OtherDetPLoanOutBoxCountScreen(viewModel) }),
        TabScreen(
            title = "Save",
            icon = CustomIcon.Vector(Icons.Default.Save),
            screen = { OtherDetPLoanOutBoxSaveScreen(viewModel) }),
        TabScreen(title = "Exit", icon = CustomIcon.Vector(Icons.AutoMirrored.Filled.ExitToApp)),
    )

}

fun getScreensByOptionType(optionType: OptionType, viewModel: BaseViewModel): List<TabScreen> {
    return when (optionType) {
        OptionType.BookIn -> ScreensDataSource.bookInScreens(viewModel as BookInViewModel)
        OptionType.BookInBox -> ScreensDataSource.bookInBoxScreens(viewModel as BookInBoxViewModel)
        OptionType.BookInCalibration -> ScreensDataSource.bookInCalScreens(viewModel as BookInCalViewModel)
        OptionType.BookInTLoan -> ScreensDataSource.bookInTLoanScreens(viewModel as TLoanViewModel)
        OptionType.BookInTLoanBox -> ScreensDataSource.bookInTLoanBoxScreens(viewModel as TLoanBoxViewModel)
        OptionType.BookInDetPLoan -> ScreensDataSource.bookInDetPLoanScreens(viewModel as DetPLoanViewModel)
        OptionType.BookInDetPLoanBox -> ScreensDataSource.bookInDetPLoanBoxScreens(viewModel as DetPLoanBoxViewModel)

        OptionType.BookOut -> ScreensDataSource.bookOutScreens(viewModel as BookOutViewModel)
        OptionType.BookOutBox -> ScreensDataSource.bookOutBoxScreens(viewModel as BookOutBoxViewModel)

        OptionType.AccountCheck -> ScreensDataSource.accountCheckScreens(viewModel as AccountCheckViewModel)

        OptionType.OnsiteCheckInOut -> ScreensDataSource.onsiteCheckInOutScreens(viewModel as OnsiteCheckInOutViewModel)
        OptionType.OnsiteVerification -> ScreensDataSource.onsiteVerificationScreens(viewModel as OnsiteVerificationViewModel)
        OptionType.OtherTLoan -> ScreensDataSource.onsiteTLoanScreens(viewModel as OtherTLoanOutViewModel)
        OptionType.OtherTLoanBox -> ScreensDataSource.onsiteTLoanBoxScreens(viewModel as OtherTLoanOutBoxViewModel)
        OptionType.OtherDetPLoan -> ScreensDataSource.onsiteDetPScreens(viewModel as OtherDetPLoanViewModel)
        OptionType.OtherDetPLoanBox -> ScreensDataSource.onsiteDetPLoanBoxScreens(viewModel as OtherDetPLoanBoxViewModel)
    }
}