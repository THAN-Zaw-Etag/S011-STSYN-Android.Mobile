package com.etag.stsyn.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Save
import com.etag.stsyn.R
import com.etag.stsyn.domain.model.BottomNavigationItem
import com.etag.stsyn.domain.model.OptionButtonModel
import com.etag.stsyn.domain.model.TabOption
import com.etag.stsyn.ui.navigation.Routes

object DataSource {
    val categories = listOf(
        OptionButtonModel(
            title = "Book Out",
            icon = R.drawable.book_out_icon,
            route = Routes.BookOutScreen.name
        ),
        OptionButtonModel(
            title = "Book In",
            icon = R.drawable.book_in_icon,
            route = Routes.BookInScreen.name
        ),
        OptionButtonModel(
            title = "Accountability Check",
            icon = R.drawable.accountability_check_icon,
            route = Routes.DetailScreen.name
        ),
        OptionButtonModel(
            title = "Other Operations",
            icon = R.drawable.other_operations_icon,
            route = Routes.OtherOperationsScreen.name
        )
    )

    val bottomNavigationItems = listOf(
        BottomNavigationItem("Home", Icons.Filled.Home, Routes.HomeScreen.name),
        BottomNavigationItem("Book Out", Icons.Filled.ArrowCircleUp, Routes.BookOutScreen.name),
        BottomNavigationItem("Book In", Icons.Filled.ArrowCircleDown, Routes.BookInScreen.name),
        BottomNavigationItem("Acct Check", Icons.Filled.Assignment, Routes.DetailScreen.name),
        BottomNavigationItem(
            "Other Operations",
            Icons.Filled.MoreHoriz,
            Routes.OtherOperationsScreen.name
        )
    )

    val bookOutOptions = listOf(
        OptionButtonModel(title = "Book Out", route = OptionType.BookOut.toString()),
        OptionButtonModel(title = "Book Out (Box)", route = OptionType.BookOutBox.toString()),
    )

    val bookInOptions = listOf(
        OptionButtonModel(title = "Book In", route = OptionType.BookIn.toString()),
        OptionButtonModel(title = "Book In (Box)", route = OptionType.BookInBox.toString()),
        OptionButtonModel(
            title = "Book In (Calibration)",
            route = OptionType.BookInCalibration.toString()
        ),
    )

    val bookInTLoanOptions = listOf(
        OptionButtonModel(title = "T-Loan", route = OptionType.BookInTLoan.toString()),
        OptionButtonModel(title = "T-Loan (Box)", route = OptionType.BookInTLoanBox.toString())
    )

    val bookInDetPLoanOptions = listOf(
        OptionButtonModel(title = "Det/T-Loan", route = OptionType.BookInDetPLoan.toString()),
        OptionButtonModel(
            title = "Det/T-Loan (Box)",
            route = OptionType.BookInDetPLoanBox.toString()
        )
    )

    val otherOperationsOptions = listOf(
        OptionButtonModel(
            title = "Onsite Check in/out",
            route = OptionType.OnsiteCheckInOut.toString()
        ),
        OptionButtonModel(
            title = "Onsite Verification",
            route = OptionType.OnsiteVerification.toString()
        )
    )


    val otherTLoanOptions = listOf(
        OptionButtonModel(title = "T-Loan", route = OptionType.OtherTLoan.toString()),
        OptionButtonModel(title = "T-Loan (Box)", route = OptionType.OtherTLoanBox.toString())
    )

    val otherDetPLoanOptions = listOf(
        OptionButtonModel(title = "Det/T-Loan", route = OptionType.OtherDetPLoan.toString()),
        OptionButtonModel(
            title = "Det/T-Loan (Box)",
            route = OptionType.OtherDetPLoanBox.toString()
        )
    )

    val tabOptions = listOf(
        TabOption(title = "ACCT CHECK", icon = Icons.Default.Assignment),
        TabOption(title = "COUNT", icon = Icons.Default.Calculate),
        TabOption(title = "SAVE", icon = Icons.Default.Save),
        TabOption(title = "EXIT", icon = Icons.Default.ExitToApp),
    )
}