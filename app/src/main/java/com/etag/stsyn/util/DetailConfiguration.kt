package com.etag.stsyn.util

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.etag.stsyn.ui.screen.CountScreen
import com.etag.stsyn.ui.screen.ScannedItemListScreen
import com.etag.stsyn.ui.screen.acct_check.AcctCheckScreen
import com.etag.stsyn.ui.screen.book_in.book_in.BookInCountScreen
import com.etag.stsyn.ui.screen.book_in.book_in.BookInSaveScreen
import com.etag.stsyn.ui.screen.book_in.book_in.BookInScanScreen
import com.etag.stsyn.ui.screen.book_in.book_in_box.BookInBoxCountScreen
import com.etag.stsyn.ui.screen.book_in.book_in_box.BookInBoxSaveScreen
import com.etag.stsyn.ui.screen.book_in.book_in_box.BookInBoxScanScreen
import com.etag.stsyn.ui.screen.book_in.det_p_loan.DetPLoanCountScreen
import com.etag.stsyn.ui.screen.book_in.det_p_loan.DetPLoanScanScreen
import com.etag.stsyn.ui.screen.book_in.det_p_loan_box.DetPLoanBoxCountScreen
import com.etag.stsyn.ui.screen.book_in.det_p_loan_box.DetPLoanBoxScanScreen
import com.etag.stsyn.ui.screen.book_in.t_loan.TLoanCountScreen
import com.etag.stsyn.ui.screen.book_in.t_loan.TLoanScanScreen
import com.etag.stsyn.ui.screen.book_in_cal.BookInCalCountScreen
import com.etag.stsyn.ui.screen.book_in_cal.BookInCalSaveScreen
import com.etag.stsyn.ui.screen.book_in_cal.BookInCalScanScreen
import com.etag.stsyn.ui.screen.book_out.book_out.BookOutSaveScreen
import com.etag.stsyn.ui.screen.book_out.book_out.BookOutScanScreen
import com.etag.stsyn.ui.screen.book_out.book_out_box.BookOutBoxCountScreen
import com.etag.stsyn.ui.screen.book_out.book_out_box.BookOutBoxSaveScreen
import com.etag.stsyn.ui.screen.book_out.book_out_box.BookOutBoxScanScreen
import com.etag.stsyn.ui.screen.book_out.book_out_box.BoxScreen
import com.etag.stsyn.ui.screen.other_operations.det_p_loan.DetPLoanScreen
import com.etag.stsyn.ui.screen.other_operations.onsite_check_in_out.CheckInOutSaveScreen
import com.etag.stsyn.ui.screen.other_operations.onsite_check_in_out.CheckInOutScreen
import com.etag.stsyn.ui.screen.other_operations.onsite_verification.OnsiteVerifyScreen
import com.etag.stsyn.ui.screen.other_operations.t_loan_out.TLoanOutSaveScreen

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
        "${OptionType.BookIn}-${tabOptions.get(1).title}" -> BookInCountScreen(
            items = listOf(
                "Hello",
                "Hello",
                "Hello",
                "Hello",
                "Hello",
                "Hello",
                "Hello",
                "Hello",
                "Hello",
                "Hello",
                "Hello",
                "Hello",
            )
        )

        "${OptionType.BookIn}-${tabOptions.get(2).title}" -> BookInSaveScreen(
            showSaveButton = true,
            onSave = {}, content = {})

        "${OptionType.BookInBox}-${tabOptions.get(0).title}" -> BookInBoxScanScreen()
        "${OptionType.BookInBox}-${tabOptions.get(1).title}" -> BookInBoxCountScreen()
        "${OptionType.BookInBox}-${tabOptions.get(2).title}" -> BookInBoxSaveScreen(
            onRefresh = { /*TODO*/ },
            onSave = {})

        "${OptionType.BookInCalibration}-${tabOptions.get(0).title}" -> BookInCalScanScreen()
        "${OptionType.BookInCalibration}-${tabOptions.get(1).title}" -> BookInCalCountScreen(items = listOf())
        "${OptionType.BookInCalibration}-${tabOptions.get(2).title}" -> BookInCalSaveScreen()

        "${OptionType.BookInTLoan}-${tabOptions.get(0).title}" -> TLoanScanScreen()
        "${OptionType.BookInTLoan}-${tabOptions.get(1).title}" -> TLoanCountScreen(
            listOf(
                "Hello",
                "Hello",
                "Hello",
                "Hello",
                "Hello",
                "Hello",
                "Hello",
                "Hello",
                "Hello",
                "Hello",
                "Hello",
                "Hello",
            )
        )
        "${OptionType.BookInTLoan}-${tabOptions.get(2).title}" -> BookInSaveScreen(
            showSaveButton = true,
            content = { /*TODO*/ }) {
        }

        "${OptionType.BookInTLoanBox}-${tabOptions.get(0).title}" -> BoxScreen(
            scannedItems = DataSource.dummyDataList,
            showBoxBookOutButton = true,
            boxOutTitle = "Box T-Loan out (3)",
            onReset = { /*TODO*/ })

        "${OptionType.BookInTLoanBox}-${tabOptions.get(1).title}" -> CountScreen(
            items = listOf("1", "2"),
            onControlTypeChange = {}
        )

        "${OptionType.BookInTLoanBox}-${tabOptions.get(2).title}" -> BookInSaveScreen(
            showSaveButton = true,
            content = { /*TODO*/ }) {
        }

        "${OptionType.BookInDetPLoan}-${tabOptions.get(0).title}" -> DetPLoanScanScreen()
        "${OptionType.BookInDetPLoan}-${tabOptions.get(1).title}" -> DetPLoanCountScreen()
        "${OptionType.BookInDetPLoan}-${tabOptions.get(2).title}" -> BookInSaveScreen(
            showSaveButton = true,
            content = { /*TODO*/ }) {
        }

        "${OptionType.BookInDetPLoanBox}-${tabOptions.get(0).title}" -> DetPLoanBoxScanScreen()
        "${OptionType.BookInDetPLoanBox}-${tabOptions.get(1).title}" -> DetPLoanBoxCountScreen()
        "${OptionType.BookInDetPLoanBox}-${tabOptions.get(2).title}" -> BookInSaveScreen(
            showSaveButton = true,
            content = { /*TODO*/ }) {
        }

        "${OptionType.AccountCheck}-${tabOptions.get(0).title}" -> AcctCheckScreen()
        "${OptionType.AccountCheck}-${tabOptions.get(1).title}" -> CountScreen(
            emptyList(),
            onControlTypeChange = {})

        "${OptionType.AccountCheck}-${tabOptions.get(2).title}" -> BookInSaveScreen(
            showSaveButton = true,
            content = { }) {
        }

        "${OptionType.OnsiteCheckInOut}-${tabOptions.get(0).title}" -> CheckInOutScreen()
        "${OptionType.OnsiteCheckInOut}-${tabOptions.get(1).title}" -> CheckInOutSaveScreen()

        "${OptionType.OnsiteVerification}-${tabOptions.get(0).title}" -> OnsiteVerifyScreen()
        "${OptionType.OnsiteVerification}-${tabOptions.get(1).title}" -> CountScreen(
            listOf(),
            onControlTypeChange = {})
        "${OptionType.OnsiteVerification}-${tabOptions.get(2).title}" -> BookInSaveScreen(
            showSaveButton = true,
            onSave = {},
            content = {})

        "${OptionType.OtherTLoan}-${tabOptions.get(0).title}" -> ScannedItemListScreen(
            items = listOf(
                "1",
                "2",
                "3"
            )
        )
        "${OptionType.OtherTLoan}-${tabOptions.get(1).title}" -> TLoanOutSaveScreen()

        "${OptionType.OtherTLoanBox}-${tabOptions.get(0).title}" -> BoxScreen(
            scannedItems = listOf("1", "2", "3"),
            onReset = { /*TODO*/ })
        "${OptionType.OtherTLoanBox}-${tabOptions.get(1).title}" -> CountScreen(
            items = listOf("1", "2", "3"),
            onControlTypeChange = {}
        )
        "${OptionType.OtherTLoanBox}-${tabOptions.get(2).title}" -> TLoanOutSaveScreen()

        "${OptionType.OtherDetPLoan}-${tabOptions.get(0).title}" -> DetPLoanScreen()
        "${OptionType.OtherDetPLoan}-${tabOptions.get(1).title}" -> TLoanOutSaveScreen()

        "${OptionType.OtherDetPLoanBox}-${tabOptions.get(0).title}" -> BoxScreen(
            scannedItems = listOf(),
            onReset = { /*TODO*/ })
        "${OptionType.OtherDetPLoanBox}-${tabOptions.get(1).title}" -> CountScreen(
            items = listOf(),
            onControlTypeChange = {}
        )
        "${OptionType.OtherDetPLoanBox}-${tabOptions.get(2).title}" -> TLoanOutSaveScreen()

        else -> Column {}
    }
}