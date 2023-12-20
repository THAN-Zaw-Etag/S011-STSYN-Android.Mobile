package com.etag.stsyn.domain.model

import androidx.annotation.DrawableRes

data class OptionButtonModel(
    val title: String,
    @DrawableRes val icon: Int? = 0,
    val route: String
)