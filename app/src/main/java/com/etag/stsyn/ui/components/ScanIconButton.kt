@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.etag.stsyn.R
import com.etag.stsyn.ui.theme.Purple80
import com.etag.stsyn.ui.theme.SuccessColor
import com.etag.stsyn.util.customShadow

@Composable
fun ScanIconButton(
    modifier: Modifier = Modifier,
    isScanning: Boolean = false,
    onScan: () -> Unit,
) {
    val animatedColor by animateColorAsState(
        targetValue = if (isScanning) SuccessColor else Color.Transparent, animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = ""
    )
    Card(
        modifier = modifier
            .padding(8.dp)
            .customShadow(
                borderRadius = 42.dp,
                color = animatedColor,
                spread = 8.dp,
                blurRadius = 5.dp
            ),
        onClick = onScan,
        shape = RoundedCornerShape(42.dp),
        colors = CardDefaults.cardColors(
            containerColor = Purple80
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.rfid_icon),
            contentDescription = null,
            modifier = Modifier
                .scale(1.5f)
                .fillMaxWidth(0.3f)
                .padding(vertical = 16.dp)
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ScanIconButtonPreview() {
    ScanIconButton(onScan = {})
}