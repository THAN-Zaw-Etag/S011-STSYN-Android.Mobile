package com.etag.stsyn.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleUp
import com.etag.stsyn.enums.OptionType
import com.etag.stsyn.ui.components.CustomIcon
import com.etag.stsyn.util.datasource.DataSource

data class TabOption(
    val title: String,
    val icon: CustomIcon,
)

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
                options.add(
                    0,
                    TabOption(optionType.title, CustomIcon.Vector(Icons.Filled.ArrowCircleUp))
                )

                options
            }

            OptionType.OtherTLoan -> {
                options.removeAt(0)
                options.add(
                    0,
                    TabOption(optionType.title, CustomIcon.Vector(Icons.Filled.ArrowCircleUp))
                )
                options
            }

            OptionType.OnsiteCheckInOut -> {
                options.removeAt(0)
                options.add(
                    0,
                    TabOption(optionType.title, CustomIcon.Vector(Icons.Filled.ArrowCircleUp))
                )

                options
            }

            OptionType.BookOut -> {
                options.removeAt(0)
                options.add(
                    0,
                    TabOption(optionType.title, CustomIcon.Vector(Icons.Filled.ArrowCircleUp))
                )

                options
            }

            else -> {
                options.add(
                    0,
                    TabOption(optionType.title, CustomIcon.Vector(Icons.Filled.ArrowCircleUp))
                )
                options
            }
        }
    }
}