package com.etag.stsyn.util

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.etag.stsyn.ui.screen.CountScreen
import com.etag.stsyn.ui.screen.ScannedItemListScreen
import com.etag.stsyn.ui.screen.book_in.BookInSaveScreen
import com.etag.stsyn.ui.screen.book_out.book_out.BookOutSaveScreen
import com.etag.stsyn.ui.screen.book_out.book_out.BookOutScreen
import com.etag.stsyn.ui.screen.book_out.book_out_box.BoxScreen

/**
 * This composable function returns content to show according to tab.
 * @param optionType Type of content to show details
 * @param tabTitle The current selected tab title
 * Example - OptionType.BookOut - BookOut returns BookOutScreen()
 * */

@Composable
fun DetailConfigurationGraph(optionType: OptionType, tabTitle: String) {
    // Declare viewmodel here

    val tabOptions = TabUtil.getTabDetails(optionType)

    return when ("$optionType-$tabTitle") {
        "${OptionType.BookOut}-${tabOptions.get(0).title}" -> BookOutScreen(
            scannedItems = emptyList(),
            onClear = {})

        "${OptionType.BookOut}-${tabOptions.get(1).title}" -> BookOutSaveScreen()
        "${OptionType.BookOutBox}-${tabOptions.get(0).title}" -> BoxScreen(
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
            ),
            onReset = {}
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
        "${OptionType.BookIn}-${tabOptions.get(0).title}" -> ScannedItemListScreen(
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

        "${OptionType.BookIn}-${tabOptions.get(1).title}" -> CountScreen(
            onControlTypeChange = {},
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
            onSave = {})

        "${OptionType.BookInBox}-${tabOptions.get(0).title}" -> BoxScreen(
            listOf("1", "2", "3"),
            showBoxBookOutButton = true,
            onReset = {})

        "${OptionType.BookInBox}-${tabOptions.get(1).title}" -> CountScreen(
            onControlTypeChange = {},
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

        "${OptionType.BookInBox}-${tabOptions.get(2).title}" -> BookOutSaveScreen()
        else -> Column {}
    }
}