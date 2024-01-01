package com.etag.stsyn.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleUp
import com.etag.stsyn.domain.model.TabIcon
import com.etag.stsyn.domain.model.TabOption


object TabUtil {

    /**
     * Used this method to filter 3 tabs options
     * OtherDetPLoan,OtherTLoan,OnSiteCheckInOut,BookOut has only 3 tabs and others have 4 tabs
     * */
    fun getTabDetails(optionType: OptionType): List<TabOption> {

        val options = DataSource.tabOptions.toMutableList()

        return when (optionType) {
            OptionType.OtherDetPLoan -> {
                options.removeAt(0)
                options.add(0, TabOption(optionType.title, TabIcon.Vector(Icons.Filled.ArrowCircleUp)))

                options
            }

            OptionType.OtherTLoan -> {
                options.removeAt(0)
                options.add(0, TabOption(optionType.title, TabIcon.Vector(Icons.Filled.ArrowCircleUp)))

                options
            }

            OptionType.OnsiteCheckInOut -> {
                options.removeAt(0)
                options.add(0, TabOption(optionType.title, TabIcon.Vector(Icons.Filled.ArrowCircleUp)))

                options
            }

            OptionType.BookOut -> {
                options.removeAt(0)
                options.add(0, TabOption(optionType.title, TabIcon.Vector(Icons.Filled.ArrowCircleUp)))

                options
            }

            else -> {
                options.add(0, TabOption(optionType.title, TabIcon.Vector(Icons.Filled.ArrowCircleUp)))

                options
            }
        }
    }
}