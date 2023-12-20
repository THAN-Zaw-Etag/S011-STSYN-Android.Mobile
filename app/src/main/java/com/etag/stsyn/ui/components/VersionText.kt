package com.etag.stsyn.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.etag.stsyn.util.AppUtil

@Composable
fun VersionText(
    color: Color = Color.Gray,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Text(
        modifier = modifier,
        color = color,
        fontSize = 14.sp,
        text = "v${AppUtil.getVersionCode(context)}",
        fontWeight = FontWeight.Bold
    )
}

@Composable
@Preview(showBackground = true)
fun VersionTextPreview() {
    VersionText()
}