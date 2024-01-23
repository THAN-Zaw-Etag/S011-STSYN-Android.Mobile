@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.etag.stsyn.ui.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.etag.stsyn.R
import com.etag.stsyn.enums.OptionType
import com.etag.stsyn.util.ReaderBatteryUtil
import com.etag.stsyn.util.datasource.DataSource
import com.etag.stsyn.util.datasource.OptionButtonModel

@Composable
fun MainScreen(
    isReaderConnected: Boolean,
    batteryPercentage: Int,
    onCategoryItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var batteryLevel by remember { mutableStateOf(0) }

    LaunchedEffect(batteryPercentage) {
        batteryLevel = batteryPercentage
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            RfidReaderStatusSection(isReaderConnected, batteryLevel)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Categories",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }
        items(DataSource.categories) {
            CategoryItem(category = it, onCategoryItemClick = onCategoryItemClick)
        }
    }
}

@Composable
private fun CategoryItem(
    category: OptionButtonModel,
    enabled: Boolean = true,
    onCategoryItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onCategoryItemClick(category.route) },
        shape = RoundedCornerShape(16.dp),
        enabled = enabled,
        modifier = Modifier.border(
            width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(16.dp)
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(horizontal = 8.dp, vertical = 24.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = category.icon!!),
                contentDescription = null,
                modifier = Modifier
                    .weight(0.2f)
                    .scale(1.5f)
            )
            Text(
                text = category.title, modifier = Modifier.weight(0.8f)
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun CategoryItemPreview() {
    CategoryItem(
        enabled = false,
        category = OptionButtonModel(
            title = "Book Out",
            icon = R.drawable.book_in_icon,
            route = OptionType.BookOut.toString()
        ), onCategoryItemClick = {}
    )
}

@Composable
private fun RfidReaderStatusSection(
    isReaderConnected: Boolean,
    batteryPercentage: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(
                0.1f
            )
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "RFID reader status", style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            if (isReaderConnected) {
                if (batteryPercentage != 0) {
                    Image(
                        painter = painterResource(id = R.drawable.bluetooth),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(
                        id = ReaderBatteryUtil.getBatteryImageByPercentage(
                            batteryPercentage
                        )
                    ), contentDescription = null, modifier = Modifier.height(24.dp)
                )
            }
        }
    }
}