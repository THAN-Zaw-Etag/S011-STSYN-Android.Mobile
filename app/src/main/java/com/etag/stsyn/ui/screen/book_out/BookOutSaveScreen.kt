package com.etag.stsyn.ui.screen.book_out

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.etag.stsyn.ui.components.UserItemLayout

@Composable
fun BookOutSaveScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        UserItemLayout(
            profile = Icons.Default.Person,
            profileType = "User",
            userName = "Admin",
            userId = "123S"
        )
    }
}