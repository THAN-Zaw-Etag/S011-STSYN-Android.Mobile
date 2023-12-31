package com.etag.stsyn.util

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Save
import androidx.compose.ui.graphics.vector.ImageVector
import com.etag.stsyn.R
import com.etag.stsyn.ui.components.CustomIcon
import com.etag.stsyn.ui.navigation.Routes
import com.etag.stsyn.ui.screen.acct_check.FilterItem

data class OptionButtonModel(
    val title: String,
    @DrawableRes val icon: Int? = 0,
    val route: String
)

data class BottomNavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String,
)

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
            route = Routes.DetailScreen.name + "/${OptionType.AccountCheck}"
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
        BottomNavigationItem(
            "Acct Check",
            Icons.Filled.Assignment,
            Routes.DetailScreen.name + "/${OptionType.AccountCheck}"
        ),
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
        OptionButtonModel(title = "Det/P-Loan", route = OptionType.BookInDetPLoan.toString()),
        OptionButtonModel(
            title = "Det/P-Loan (Box)",
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
        OptionButtonModel(title = "Det/P-Loan", route = OptionType.OtherDetPLoan.toString()),
        OptionButtonModel(
            title = "Det/P-Loan (Box)",
            route = OptionType.OtherDetPLoanBox.toString()
        )
    )

    val tabOptions = listOf(
        TabOption(title = "COUNT", icon = CustomIcon.Resource(R.drawable.tally)),
        TabOption(title = "SAVE", icon = CustomIcon.Vector(Icons.Default.Save)),
        TabOption(title = "EXIT", icon = CustomIcon.Vector(Icons.Default.ExitToApp)),
    )

    val filters = listOf(
        FilterItem("Store Type", "-"),
        FilterItem("CS Number", "-"),
        FilterItem("Unit/SQN", "-"),
        FilterItem("Flight", "-"),
        FilterItem("Item Type", "-"),
        FilterItem("Location", "-"),
        FilterItem("Box", "-"),
        FilterItem("Remarks", "-"),
    )

    val dummyDataList = listOf("One", "Two", "Three", "Four", "Five")
}