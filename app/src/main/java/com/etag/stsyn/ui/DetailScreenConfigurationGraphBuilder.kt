package com.etag.stsyn.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.etag.stsyn.domain.model.TabOption
import com.etag.stsyn.ui.screen.acct_check.AcctCheckCountScreen
import com.etag.stsyn.ui.screen.acct_check.AcctCheckSaveScreen
import com.etag.stsyn.ui.screen.acct_check.AcctCheckScreen
import com.etag.stsyn.ui.screen.book_in.book_in.BookInCountScreen
import com.etag.stsyn.ui.screen.book_in.book_in.BookInSaveScreen
import com.etag.stsyn.ui.screen.book_in.book_in.BookInScanScreen
import com.etag.stsyn.ui.screen.book_in.book_in_box.BookInBoxCountScreen
import com.etag.stsyn.ui.screen.book_in.book_in_box.BookInBoxSaveScreen
import com.etag.stsyn.ui.screen.book_in.book_in_box.BookInBoxScanScreen
import com.etag.stsyn.ui.screen.book_in.det_p_loan.DetPLoanCountScreen
import com.etag.stsyn.ui.screen.book_in.det_p_loan.DetPLoanSaveScreen
import com.etag.stsyn.ui.screen.book_in.det_p_loan.DetPLoanScanScreen
import com.etag.stsyn.ui.screen.book_in.det_p_loan_box.DetPLoanBoxCountScreen
import com.etag.stsyn.ui.screen.book_in.det_p_loan_box.DetPLoanBoxSaveScreen
import com.etag.stsyn.ui.screen.book_in.det_p_loan_box.DetPLoanBoxScanScreen
import com.etag.stsyn.ui.screen.book_in.t_loan.TLoanCountScreen
import com.etag.stsyn.ui.screen.book_in.t_loan.TLoanSaveScreen
import com.etag.stsyn.ui.screen.book_in.t_loan.TLoanScanScreen
import com.etag.stsyn.ui.screen.book_in.t_loan_box.TLoanBoxCountScreen
import com.etag.stsyn.ui.screen.book_in.t_loan_box.TLoanBoxSaveScreen
import com.etag.stsyn.ui.screen.book_in.t_loan_box.TLoanBoxScanScreen
import com.etag.stsyn.ui.screen.book_in_cal.BookInCalCountScreen
import com.etag.stsyn.ui.screen.book_in_cal.BookInCalSaveScreen
import com.etag.stsyn.ui.screen.book_in_cal.BookInCalScanScreen
import com.etag.stsyn.ui.screen.book_out.book_out.BookOutSaveScreen
import com.etag.stsyn.ui.screen.book_out.book_out.BookOutScanScreen
import com.etag.stsyn.ui.screen.book_out.book_out_box.BookOutBoxCountScreen
import com.etag.stsyn.ui.screen.book_out.book_out_box.BookOutBoxSaveScreen
import com.etag.stsyn.ui.screen.book_out.book_out_box.BookOutBoxScanScreen
import com.etag.stsyn.ui.screen.other_operations.det_p_loan.OtherDetPLoanOutSaveScreen
import com.etag.stsyn.ui.screen.other_operations.det_p_loan.OtherDetPLoanScanScreen
import com.etag.stsyn.ui.screen.other_operations.det_p_loan_box.OtherDetPLoanOutBoxCountScreen
import com.etag.stsyn.ui.screen.other_operations.det_p_loan_box.OtherDetPLoanOutBoxSaveScreen
import com.etag.stsyn.ui.screen.other_operations.det_p_loan_box.OtherDetPLoanOutBoxScanScreen
import com.etag.stsyn.ui.screen.other_operations.onsite_check_in_out.CheckInOutSaveScreen
import com.etag.stsyn.ui.screen.other_operations.onsite_check_in_out.CheckInOutScreen
import com.etag.stsyn.ui.screen.other_operations.onsite_verification.OnsiteVerificationCountScreen
import com.etag.stsyn.ui.screen.other_operations.onsite_verification.OnsiteVerificationSaveScreen
import com.etag.stsyn.ui.screen.other_operations.onsite_verification.OnsiteVerifyScreen
import com.etag.stsyn.ui.screen.other_operations.t_loan_out.OtherTLoanOutSaveScreen
import com.etag.stsyn.ui.screen.other_operations.t_loan_out.OtherTLoanOutScanScreen
import com.etag.stsyn.ui.screen.other_operations.t_loan_out_box.OtherTLoanOutBoxCountScreen
import com.etag.stsyn.ui.screen.other_operations.t_loan_out_box.OtherTLoanOutBoxScanScreen
import com.etag.stsyn.ui.viewmodel.RfidViewModel
import com.etag.stsyn.ui.viewmodel.SharedUiViewModel
import com.etag.stsyn.util.OptionType
import com.etag.stsyn.util.TabUtil

/**
 * This composable function returns content to show according to tab.
 * @param optionType Type of content to show details
 * @param tabTitle The current selected tab title
 * Example - OptionType.BookOut - BookOut returns BookOutScreen()
 * */

object DetailScreenConfigurationGraphBuilder {
    val LocalRfidViewModel =
        staticCompositionLocalOf<RfidViewModel> { error("No viewmodel found!") }

    @Composable
    fun build(
        tabOptions: List<TabOption>,
        optionType: OptionType,
        tabTitle: String,
        rfidViewModel: RfidViewModel
    ) {

        var screen = Column {}

        CompositionLocalProvider(LocalRfidViewModel provides rfidViewModel) {
            when ("$optionType-$tabTitle") {
                "${OptionType.BookOut}-${tabOptions.get(0).title}" -> screen = BookOutScanScreen()
                "${OptionType.BookOut}-${tabOptions.get(1).title}" -> screen = BookOutSaveScreen()

                "${OptionType.BookOutBox}-${tabOptions.get(0).title}" -> screen =
                    BookOutBoxScanScreen()

                "${OptionType.BookOutBox}-${tabOptions.get(1).title}" -> screen =
                    BookOutBoxCountScreen()

                "${OptionType.BookOutBox}-${tabOptions.get(2).title}" -> screen =
                    BookOutBoxSaveScreen()

                "${OptionType.BookIn}-${tabOptions.get(0).title}" -> screen = BookInScanScreen()
                "${OptionType.BookIn}-${tabOptions.get(1).title}" -> screen = BookInCountScreen()
                "${OptionType.BookIn}-${tabOptions.get(2).title}" -> screen = BookInSaveScreen(
                    showSaveButton = true,
                    onSave = {},
                    content = {})

                "${OptionType.BookInBox}-${tabOptions.get(0).title}" -> screen =
                    BookInBoxScanScreen()

                "${OptionType.BookInBox}-${tabOptions.get(1).title}" -> screen =
                    BookInBoxCountScreen()

                "${OptionType.BookInBox}-${tabOptions.get(2).title}" -> screen =
                    BookInBoxSaveScreen(
                        onRefresh = { /*TODO*/ },
                        onSave = {})

                "${OptionType.BookInCalibration}-${tabOptions.get(0).title}" -> screen =
                    BookInCalScanScreen()

                "${OptionType.BookInCalibration}-${tabOptions.get(1).title}" -> screen =
                    BookInCalCountScreen(
                        items = listOf()
                    )

                "${OptionType.BookInCalibration}-${tabOptions.get(2).title}" -> screen =
                    BookInCalSaveScreen()

                "${OptionType.BookInTLoan}-${tabOptions.get(0).title}" -> screen = TLoanScanScreen()
                "${OptionType.BookInTLoan}-${tabOptions.get(1).title}" -> screen =
                    TLoanCountScreen()

                "${OptionType.BookInTLoan}-${tabOptions.get(2).title}" -> screen = TLoanSaveScreen()

                "${OptionType.BookInTLoanBox}-${tabOptions.get(0).title}" -> screen =
                    TLoanBoxScanScreen()

                "${OptionType.BookInTLoanBox}-${tabOptions.get(1).title}" -> screen =
                    TLoanBoxCountScreen()

                "${OptionType.BookInTLoanBox}-${tabOptions.get(2).title}" -> screen =
                    TLoanBoxSaveScreen()

                "${OptionType.BookInDetPLoan}-${tabOptions.get(0).title}" -> screen =
                    DetPLoanScanScreen()

                "${OptionType.BookInDetPLoan}-${tabOptions.get(1).title}" -> screen =
                    DetPLoanCountScreen()

                "${OptionType.BookInDetPLoan}-${tabOptions.get(2).title}" -> screen =
                    DetPLoanSaveScreen()

                "${OptionType.BookInDetPLoanBox}-${tabOptions.get(0).title}" -> screen =
                    DetPLoanBoxScanScreen()

                "${OptionType.BookInDetPLoanBox}-${tabOptions.get(1).title}" -> screen =
                    DetPLoanBoxCountScreen()

                "${OptionType.BookInDetPLoanBox}-${tabOptions.get(2).title}" -> screen =
                    DetPLoanBoxSaveScreen()

                "${OptionType.AccountCheck}-${tabOptions.get(0).title}" -> screen =
                    AcctCheckScreen()

                "${OptionType.AccountCheck}-${tabOptions.get(1).title}" -> screen =
                    AcctCheckCountScreen()

                "${OptionType.AccountCheck}-${tabOptions.get(2).title}" -> screen =
                    AcctCheckSaveScreen()

                "${OptionType.OnsiteCheckInOut}-${tabOptions.get(0).title}" -> screen =
                    CheckInOutScreen()

                "${OptionType.OnsiteCheckInOut}-${tabOptions.get(1).title}" -> screen =
                    CheckInOutSaveScreen()

                "${OptionType.OnsiteVerification}-${tabOptions.get(0).title}" -> screen =
                    OnsiteVerifyScreen()

                "${OptionType.OnsiteVerification}-${tabOptions.get(1).title}" -> screen =
                    OnsiteVerificationCountScreen()

                "${OptionType.OnsiteVerification}-${tabOptions.get(2).title}" -> screen =
                    OnsiteVerificationSaveScreen()

                "${OptionType.OtherTLoan}-${tabOptions.get(0).title}" -> screen =
                    OtherTLoanOutScanScreen()

                "${OptionType.OtherTLoan}-${tabOptions.get(1).title}" -> screen =
                    OtherTLoanOutSaveScreen()

                "${OptionType.OtherTLoanBox}-${tabOptions.get(0).title}" -> screen =
                    OtherTLoanOutBoxScanScreen()

                "${OptionType.OtherTLoanBox}-${tabOptions.get(1).title}" -> screen =
                    OtherTLoanOutBoxCountScreen()

                "${OptionType.OtherTLoanBox}-${tabOptions.get(2).title}" -> screen =
                    OtherDetPLoanOutBoxSaveScreen()

                "${OptionType.OtherDetPLoan}-${tabOptions.get(0).title}" -> screen =
                    OtherDetPLoanScanScreen()

                "${OptionType.OtherDetPLoan}-${tabOptions.get(1).title}" -> screen =
                    OtherDetPLoanOutSaveScreen()

                "${OptionType.OtherDetPLoanBox}-${tabOptions.get(0).title}" -> screen =
                    OtherDetPLoanOutBoxScanScreen()

                "${OptionType.OtherDetPLoanBox}-${tabOptions.get(1).title}" -> screen =
                    OtherDetPLoanOutBoxCountScreen()

                "${OptionType.OtherDetPLoanBox}-${tabOptions.get(2).title}" -> screen =
                    OtherTLoanOutSaveScreen()

                else -> Column {}
            }
        }

        return screen
    }
}

