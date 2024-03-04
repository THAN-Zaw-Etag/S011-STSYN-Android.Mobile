package com.etag.stsyn.ui.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
fun OtherOperationsScreen(
    menuAccessRight: MenuAccessRight,
    onOptionButtonClick: (OptionType) -> Unit,
    modifier: Modifier = Modifier
) {
    val optionPairs = hashMapOf(
        OptionType.OnsiteVerification.title to menuAccessRight.allowOnSiteVerification,
        OptionType.OnsiteCheckInOut.title to menuAccessRight.allowOnSiteTransfer,
        OptionType.OtherTLoan.title to menuAccessRight.allowTLoan,
        OptionType.OtherTLoanBox.title to menuAccessRight.allowTLoanBox,
        OptionType.OtherDetPLoan.title to menuAccessRight.allowPermanentLoan,
        OptionType.OtherDetPLoanBox.title to menuAccessRight.allowPermanentLoanBox
    )

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DataSource.otherOperationsOptions.forEach {
                    OptionButtonLayout(
                        showButton = optionPairs.getValue(it.title),
                        optionButtonModel = it,
                        onOptionButtonClick = {
                            onOptionButtonClick(
                                OptionType.valueOf(it)
                            )
                        })
                }

                OptionsButtonRow(
                    text = "T-Loan Out",
                    optionPairs = optionPairs,
                    optionButtonModels = DataSource.otherTLoanOptions,
                    onOptionItemClick = {
                        onOptionButtonClick(
                            OptionType.valueOf(it)
                        )
                    })

                OptionsButtonRow(
                    text = "Det/P-Loan Out",
                    optionPairs = optionPairs,
                    optionButtonModels = DataSource.otherDetPLoanOptions,
                    onOptionItemClick = {
                        onOptionButtonClick(
                            OptionType.valueOf(it)
                        )
                    })
            }
        }
    }
}