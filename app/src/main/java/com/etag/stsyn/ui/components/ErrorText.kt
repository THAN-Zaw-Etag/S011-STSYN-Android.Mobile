package com.etag.stsyn.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.etag.stsyn.ui.theme.errorColor
import com.etag.stsyn.util.ErrorMessages

@Composable
fun ErrorText(
    text: String,
    textAlign: TextAlign = TextAlign.Center,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = errorColor,
        modifier = modifier.fillMaxWidth(),
        textAlign = textAlign
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ErrorTextPreview() {
    ErrorText(text = ErrorMessages.READ_AN_ITEM_FIRST)
}