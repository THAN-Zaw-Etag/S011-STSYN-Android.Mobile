package com.etag.stsyn.util

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.etag.stsyn.ui.screen.acct_check.AcctCheckCountScreen
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

/**
 * This composable function returns content to show according to tab.
 * @param optionType Type of content to show details
 * @param tabTitle The current selected tab title
 * Example - OptionType.BookOut - BookOut returns BookOutScreen()
 * */

@Composable
fun DetailConfigurationGraph(optionType: OptionType, tabTitle: String) {

    val tabOptions = TabUtil.getTabDetails(optionType)

    return when ("$optionType-$tabTitle") {
        "${OptionType.BookOut}-${tabOptions.get(0).title}" -> BookOutScanScreen()
        "${OptionType.BookOut}-${tabOptions.get(1).title}" -> BookOutSaveScreen()

        "${OptionType.BookOutBox}-${tabOptions.get(0).title}" -> BookOutBoxScanScreen()
        "${OptionType.BookOutBox}-${tabOptions.get(1).title}" -> BookOutBoxCountScreen()
        "${OptionType.BookOutBox}-${tabOptions.get(2).title}" -> BookOutBoxSaveScreen()

        "${OptionType.BookIn}-${tabOptions.get(0).title}" -> BookInScanScreen()
        "${OptionType.BookIn}-${tabOptions.get(1).title}" -> BookInCountScreen()
        "${OptionType.BookIn}-${tabOptions.get(2).title}" -> BookInSaveScreen(
            showSaveButton = true,
            onSave = {},
            content = {})

        "${OptionType.BookInBox}-${tabOptions.get(0).title}" -> BookInBoxScanScreen()
        "${OptionType.BookInBox}-${tabOptions.get(1).title}" -> BookInBoxCountScreen()
        "${OptionType.BookInBox}-${tabOptions.get(2).title}" -> BookInBoxSaveScreen(
            onRefresh = { /*TODO*/ },
            onSave = {})

        "${OptionType.BookInCalibration}-${tabOptions.get(0).title}" -> BookInCalScanScreen()
        "${OptionType.BookInCalibration}-${tabOptions.get(1).title}" -> BookInCalCountScreen(items = listOf())
        "${OptionType.BookInCalibration}-${tabOptions.get(2).title}" -> BookInCalSaveScreen()

        "${OptionType.BookInTLoan}-${tabOptions.get(0).title}" -> TLoanScanScreen()
        "${OptionType.BookInTLoan}-${tabOptions.get(1).title}" -> TLoanCountScreen()
        "${OptionType.BookInTLoan}-${tabOptions.get(2).title}" -> TLoanSaveScreen()

        "${OptionType.BookInTLoanBox}-${tabOptions.get(0).title}" -> TLoanBoxScanScreen()
        "${OptionType.BookInTLoanBox}-${tabOptions.get(1).title}" -> TLoanBoxCountScreen()
        "${OptionType.BookInTLoanBox}-${tabOptions.get(2).title}" -> TLoanBoxSaveScreen()

        "${OptionType.BookInDetPLoan}-${tabOptions.get(0).title}" -> DetPLoanScanScreen()
        "${OptionType.BookInDetPLoan}-${tabOptions.get(1).title}" -> DetPLoanCountScreen()
        "${OptionType.BookInDetPLoan}-${tabOptions.get(2).title}" -> DetPLoanSaveScreen()

        "${OptionType.BookInDetPLoanBox}-${tabOptions.get(0).title}" -> DetPLoanBoxScanScreen()
        "${OptionType.BookInDetPLoanBox}-${tabOptions.get(1).title}" -> DetPLoanBoxCountScreen()
        "${OptionType.BookInDetPLoanBox}-${tabOptions.get(2).title}" -> DetPLoanBoxSaveScreen()

        "${OptionType.AccountCheck}-${tabOptions.get(0).title}" -> AcctCheckScreen()
        "${OptionType.AccountCheck}-${tabOptions.get(1).title}" -> AcctCheckCountScreen()
        "${OptionType.AccountCheck}-${tabOptions.get(2).title}" -> AcctCheckScreen()

        "${OptionType.OnsiteCheckInOut}-${tabOptions.get(0).title}" -> CheckInOutScreen()
        "${OptionType.OnsiteCheckInOut}-${tabOptions.get(1).title}" -> CheckInOutSaveScreen()

        "${OptionType.OnsiteVerification}-${tabOptions.get(0).title}" -> OnsiteVerifyScreen()
        "${OptionType.OnsiteVerification}-${tabOptions.get(1).title}" -> OnsiteVerificationCountScreen()
        "${OptionType.OnsiteVerification}-${tabOptions.get(2).title}" -> OnsiteVerificationSaveScreen()

        "${OptionType.OtherTLoan}-${tabOptions.get(0).title}" -> OtherTLoanOutScanScreen()
        "${OptionType.OtherTLoan}-${tabOptions.get(1).title}" -> OtherTLoanOutSaveScreen()

        "${OptionType.OtherTLoanBox}-${tabOptions.get(0).title}" -> OtherTLoanOutBoxScanScreen()
        "${OptionType.OtherTLoanBox}-${tabOptions.get(1).title}" -> OtherTLoanOutBoxCountScreen()
        "${OptionType.OtherTLoanBox}-${tabOptions.get(2).title}" -> OtherDetPLoanOutBoxSaveScreen()

        "${OptionType.OtherDetPLoan}-${tabOptions.get(0).title}" -> OtherDetPLoanScanScreen()
        "${OptionType.OtherDetPLoan}-${tabOptions.get(1).title}" -> OtherDetPLoanOutSaveScreen()

        "${OptionType.OtherDetPLoanBox}-${tabOptions.get(0).title}" -> OtherDetPLoanOutBoxScanScreen()
        "${OptionType.OtherDetPLoanBox}-${tabOptions.get(1).title}" -> OtherDetPLoanOutBoxCountScreen()
        "${OptionType.OtherDetPLoanBox}-${tabOptions.get(2).title}" -> OtherTLoanOutSaveScreen()

        else -> Column {}
    }
}