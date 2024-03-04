package com.etag.stsyn.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

fun <T> LazyListScope.listItems(
    items: List<T>,
    key: ((item: T) -> Any)? = null,
    contentType: (item: T) -> Any? = { null },
    itemContent: @Composable LazyItemScope.(item: T) -> Unit
) = if (items.isEmpty()) item {
    Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "No item!")
    }
}
else {
    items(count = items.size,
        key = if (key != null) { index: Int -> key(items[index]) } else null,
        contentType = { index: Int -> contentType(items[index]) }) {
        itemContent(items[it])
    }
}

fun <T> LazyListScope.listItemsIndexed(
    items: List<T>,
    key: ((index: Int, item: T) -> Any)? = null,
    contentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) = if (items.isEmpty()) item {
    Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "No item!")
    }
} else {
    items(count = items.size,
        key = if (key != null) { index: Int -> key(index, items[index]) } else null,
        contentType = { index -> contentType(index, items[index]) }) {
        itemContent(it, items[it])
    }
}

@SuppressLint("ComposableNaming")
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun test() {
    val list = emptyList<String>()
    Box {
        LazyColumn {
            listItems(list) {
                Text(text = it)
            }
        }
    }
}