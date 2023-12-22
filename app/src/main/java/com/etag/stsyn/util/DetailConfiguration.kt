package com.etag.stsyn.util

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.etag.stsyn.ui.screen.CountScreen
import com.etag.stsyn.ui.screen.book_out.book_out.BookOutSaveScreen
import com.etag.stsyn.ui.screen.book_out.book_out.BookOutScreen
import com.etag.stsyn.ui.screen.book_out.book_out_box.BookOutBoxScreen

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
        "${OptionType.BookOut}-${tabOptions.get(0).title}" -> BookOutScreen(
            scannedItems = emptyList(),
            onClear = {})

        "${OptionType.BookOut}-${tabOptions.get(1).title}" -> BookOutSaveScreen()
        "${OptionType.BookOutBox}-${tabOptions.get(0).title}" -> BookOutBoxScreen(
            listOf(
                "Hello",
                "World",
                "You",
                "Are",
                "Welcome",
                "Nice",
                "To",
                "Meet",
                "You"
            )
        )

        "${OptionType.BookOutBox}-${tabOptions.get(1).title}" -> CountScreen(
            items = listOf(
                "SN001001 – R0101",
                "SN001001 – R0101",
                "SN001001 – R0101",
                "SN001001 – R0101",
                "SN001001 – R0101",
                "SN001001 – R0101",
                "SN001001 – R0101",
                "SN001001 – R0101",
                "SN001001 – R0101",
                "SN001001 – R0101",
                "SN001001 – R0101",
                "SN001001 – R0101"
            ), onControlTypeChange = {})

        "${OptionType.BookOutBox}-${tabOptions.get(2).title}" -> BookOutSaveScreen()
        else -> Column {}
    }
}