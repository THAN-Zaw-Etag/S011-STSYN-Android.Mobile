package com.etag.stsyn.domain.model

import androidx.annotation.DrawableRes

data class Category(
    val title: String,
    @DrawableRes val icon: Int,
    val type: CategoryType
)

enum class CategoryType {
    BookOut, BookIn, AccountabilityCheck, OtherOperations
}
