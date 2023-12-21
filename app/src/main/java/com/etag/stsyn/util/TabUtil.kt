package com.etag.stsyn.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleUp
import com.etag.stsyn.domain.model.TabOption

object TabUtil {
    fun getTabDetails(optionType: OptionType): List<TabOption> {

        val options = DataSource.tabOptions.toMutableList()

        return when (optionType) {
            OptionType.OtherDetPLoan -> {
                options.removeAt(1)
                options.add(0, TabOption(optionType.title, Icons.Filled.ArrowCircleUp))

                options
            }

            OptionType.OtherTLoan -> {
                options.removeAt(1)
                options.add(0, TabOption(optionType.title, Icons.Filled.ArrowCircleUp))

                options
            }

            OptionType.OnsiteCheckInOut -> {
                options.removeAt(1)
                options.add(0, TabOption(optionType.title, Icons.Filled.ArrowCircleUp))

                options
            }

            OptionType.BookOut -> {
                options.removeAt(1)
                options.add(0, TabOption(optionType.title, Icons.Filled.ArrowCircleUp))

                options
            }

            else -> {
                options.add(0, TabOption(optionType.title, Icons.Filled.ArrowCircleUp))

                options
            }
        }
    }
}