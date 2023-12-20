package com.etag.stsyn.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.ui.components.OptionButtonLayout
import com.etag.stsyn.ui.components.OptionsButtonRow
import com.etag.stsyn.util.DataSource
import com.etag.stsyn.util.OptionType

@Composable
fun OtherOperationsScreen(
    onOptionButtonClick: (OptionType) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DataSource.otherOperationsOptions.forEach {
                    OptionButtonLayout(optionButtonModel = it, onOptionButtonClick = {
                        onOptionButtonClick(
                            OptionType.valueOf(it)
                        )
                    })
                }

                OptionsButtonRow(
                    text = "T-Loan Out",
                    optionButtonModels = DataSource.otherTLoanOptions,
                    onOptionItemClick = {
                        onOptionButtonClick(
                            OptionType.valueOf(it)
                        )
                    })

                OptionsButtonRow(
                    text = "Det/P-Loan Out",
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