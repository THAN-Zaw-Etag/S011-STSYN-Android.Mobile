package com.etag.stsyn.ui.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.enums.OptionType
import com.etag.stsyn.ui.components.OptionButtonLayout
import com.etag.stsyn.ui.components.OptionsButtonRow
import com.etag.stsyn.util.datasource.DataSource
import com.tzh.retrofit_module.domain.model.login.MenuAccessRight

@Composable
fun BookInScreen(
    menuAccessRight: MenuAccessRight,
    onOptionButtonClick: (OptionType) -> Unit,
    modifier: Modifier = Modifier
) {

    val optionPairs = hashMapOf(
        OptionType.BookIn.title to menuAccessRight.allowBookIn,
        OptionType.BookInBox.title to menuAccessRight.allowBookInBox,
        OptionType.BookInCalibration.title to menuAccessRight.allowBookInCal,
        OptionType.BookInTLoan.title to menuAccessRight.allowBookInT_Loan,
        OptionType.BookInTLoanBox.title to menuAccessRight.allowBookInT_LoanBox,
        OptionType.BookInDetPLoan.title to menuAccessRight.allowBookInP_Loan,
        OptionType.BookInDetPLoanBox.title to menuAccessRight.allowBookInP_LoanBox
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DataSource.bookInOptions.forEach {
                    OptionButtonLayout(
                        showButton = optionPairs.getValue(it.title),
                        optionButtonModel = it,
                        onOptionButtonClick = { onOptionButtonClick(OptionType.valueOf(it)) })
                }

                OptionsButtonRow(
                    text = "Book In (T-Loan)",
                    optionPairs = optionPairs,
                    optionButtonModels = DataSource.bookInTLoanOptions,
                    onOptionItemClick = { onOptionButtonClick(OptionType.valueOf(it)) })
                OptionsButtonRow(
                    text = "Book In (Det/P-Loan)",
                    optionPairs = optionPairs,
                    optionButtonModels = DataSource.bookInDetPLoanOptions,
                    onOptionItemClick = { onOptionButtonClick(OptionType.valueOf(it)) })
            }
        }
    }
}