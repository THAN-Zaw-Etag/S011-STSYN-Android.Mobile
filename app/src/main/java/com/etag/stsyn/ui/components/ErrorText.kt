package com.etag.stsyn.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.etag.stsyn.ui.theme.errorColor

@Composable
fun ErrorText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = errorColor,
        modifier = modifier
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ErrorTextPreview() {
    ErrorText(text = "Please read an item first!")
}