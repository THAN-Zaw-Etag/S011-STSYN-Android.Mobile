package com.etag.stsyn.util.datasource

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Save
import com.etag.stsyn.R
import com.etag.stsyn.enums.OptionType
import com.etag.stsyn.ui.components.CustomIcon
import com.etag.stsyn.ui.navigation.Routes
import com.etag.stsyn.util.TabOption
import com.tzh.retrofit_module.domain.model.FilterItem

data class OptionButtonModel(
    val title: String,
    @DrawableRes val icon: Int? = 0,
    val route: String
)

data class BottomNavigationItem(
    val title: String,
    val icon: CustomIcon,
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
            title = "Calibration",
            icon = R.drawable.calibration_icon,
            route = Routes.DetailScreen.name + "/${OptionType.AccountCheck}"
        ),
        OptionButtonModel(
            title = "Other Operations",
            icon = R.drawable.other_operations_icon,
            route = Routes.OtherOperationsScreen.name
        )
    )

    val bottomNavigationItems = listOf(
        BottomNavigationItem("Home", CustomIcon.Vector(Icons.Filled.Home), Routes.HomeScreen.name),
        BottomNavigationItem(
            "Book Out",
            CustomIcon.Vector(Icons.Filled.ArrowCircleUp),
            Routes.BookOutScreen.name
        ),
        BottomNavigationItem(
            "Book In",
            CustomIcon.Vector(Icons.Filled.ArrowCircleDown),
            Routes.BookInScreen.name
        ),
        BottomNavigationItem(
            "Calibration",
            CustomIcon.Resource(R.drawable.calibration_icon),
            Routes.DetailScreen.name + "/${OptionType.AccountCheck}"
        ),
        BottomNavigationItem(
            "Other Operations",
            CustomIcon.Vector(Icons.Filled.MoreHoriz),
            Routes.OtherOperationsScreen.name
        )
    )

    val bookOutOptions = listOf(
        OptionButtonModel(title = "Book Out", route = OptionType.BookOut.toString()),
        OptionButtonModel(title = "Book Out (Box)", route = OptionType.BookOutBox.toString()),
    )

    val bookInOptions = listOf(
        OptionButtonModel(title = OptionType.BookIn.title, route = OptionType.BookIn.toString()),
        OptionButtonModel(
            title = OptionType.BookInBox.title,
            route = OptionType.BookInBox.toString()
        ),
        OptionButtonModel(
            title = OptionType.BookInCalibration.title,
            route = OptionType.BookInCalibration.toString()
        ),
    )

    val bookInTLoanOptions = listOf(
        OptionButtonModel(
            title = OptionType.BookInTLoan.title,
            route = OptionType.BookInTLoan.toString()
        ),
        OptionButtonModel(
            title = OptionType.BookInTLoanBox.title,
            route = OptionType.BookInTLoanBox.toString()
        )
    )

    val bookInDetPLoanOptions = listOf(
        OptionButtonModel(
            title = OptionType.BookInDetPLoan.title,
            route = OptionType.BookInDetPLoan.toString()
        ),
        OptionButtonModel(
            title = OptionType.BookInDetPLoanBox.title,
            route = OptionType.BookInDetPLoanBox.toString()
        )
    )

    val otherOperationsOptions = listOf(
        OptionButtonModel(
            title = OptionType.OnsiteCheckInOut.title,
            route = OptionType.OnsiteCheckInOut.toString()
        ),
        OptionButtonModel(
            title = OptionType.OnsiteVerification.title,
            route = OptionType.OnsiteVerification.toString()
        )
    )

    val otherTLoanOptions = listOf(
        OptionButtonModel(
            title = OptionType.OtherTLoan.title,
            route = OptionType.OtherTLoan.toString()
        ),
        OptionButtonModel(
            title = OptionType.OtherTLoanBox.title,
            route = OptionType.OtherTLoanBox.toString()
        )
    )

    val otherDetPLoanOptions = listOf(
        OptionButtonModel(
            title = OptionType.OtherDetPLoan.title,
            route = OptionType.OtherDetPLoan.toString()
        ),
        OptionButtonModel(
            title = OptionType.OtherDetPLoanBox.title,
            route = OptionType.OtherDetPLoanBox.toString()
        )
    )

    val tabOptions = listOf(
        TabOption(title = "COUNT", icon = CustomIcon.Resource(R.drawable.tally)),
        TabOption(title = "SAVE", icon = CustomIcon.Vector(Icons.Default.Save)),
        TabOption(title = "EXIT", icon = CustomIcon.Vector(Icons.AutoMirrored.Filled.ExitToApp)),
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