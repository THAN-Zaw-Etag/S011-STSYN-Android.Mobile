package com.etag.stsyn.util

import com.etag.stsyn.R
import com.etag.stsyn.domain.model.Category
import com.etag.stsyn.domain.model.CategoryType

object DataSource {
    val categories = listOf(
        Category(title = "Book Out", icon = R.drawable.book_out_icon, type = CategoryType.BookOut),
        Category(title = "Book In", icon = R.drawable.book_in_icon, type = CategoryType.BookIn),
        Category(
            title = "Accountability Check",
            icon = R.drawable.accountability_check_icon,
            type = CategoryType.AccountabilityCheck
        ),
        Category(
            title = "Other Operations",
            icon = R.drawable.other_operations_icon,
            type = CategoryType.AccountabilityCheck
        )
    )
}