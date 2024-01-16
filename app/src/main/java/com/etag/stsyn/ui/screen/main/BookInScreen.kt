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

@Composable
fun BookInScreen(
    onOptionButtonClick: (OptionType) -> Unit,
    modifier: Modifier = Modifier
) {
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
                        optionButtonModel = it,
                        onOptionButtonClick = { onOptionButtonClick(OptionType.valueOf(it)) })
                }

                OptionsButtonRow(
                    text = "Book In (T-Loan)",
                    optionButtonModels = DataSource.bookInTLoanOptions,
                    onOptionItemClick = { onOptionButtonClick(OptionType.valueOf(it)) })
                OptionsButtonRow(
                    text = "Book In (Det/P-Loan)",
                    optionButtonModels = DataSource.bookInDetPLoanOptions,
                    onOptionItemClick = { onOptionButtonClick(OptionType.valueOf(it)) })
            }
        }
    }
}