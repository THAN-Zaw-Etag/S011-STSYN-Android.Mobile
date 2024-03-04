package com.etag.stsyn.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.etag.stsyn.R
import com.etag.stsyn.ui.theme.Purple80

@Composable
fun SODInitiateScreen(
    modifier: Modifier = Modifier,
    onSodInitiate: () -> Unit
) {
    Column(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.main_upper),
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth,
            contentDescription = null
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Store Management System",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Purple80,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.main_lower),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .rotate(180f)
                    .wrapContentHeight()
            )

            TextButton(onClick = onSodInitiate, modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = "Please initiate Start-of-Day",
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}