package com.etag.stsyn.ui.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.etag.stsyn.enums.OptionType
import com.etag.stsyn.ui.components.OptionButtonLayout
import com.etag.stsyn.util.datasource.DataSource
import com.tzh.retrofit_module.domain.model.login.MenuAccessRight

@Composable
fun BookOutScreen(
    menuAccessRight: MenuAccessRight,
    onOptionButtonClick: (OptionType) -> Unit,
    modifier: Modifier = Modifier
) {
    val optionPairs = hashMapOf(
        OptionType.BookOut.title to menuAccessRight.allowBookOut,
        OptionType.BookOutBox.title to menuAccessRight.allowBookOutBox
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DataSource.bookOutOptions.forEach {
            OptionButtonLayout(
                showButton = optionPairs.getValue(it.title),
                optionButtonModel = it,
                onOptionButtonClick = {
                    onOptionButtonClick((OptionType.valueOf(it)))
                })
        }
    }
}
