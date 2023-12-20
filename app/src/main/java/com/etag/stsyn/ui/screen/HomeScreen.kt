@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.etag.stsyn.R
import com.etag.stsyn.domain.model.Category
import com.etag.stsyn.domain.model.CategoryType
import com.etag.stsyn.ui.components.AppBar
import com.etag.stsyn.ui.components.ProfileTextButton
import com.etag.stsyn.ui.components.VersionText
import com.etag.stsyn.util.BatteryImageUtil
import com.etag.stsyn.util.DataSource
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onLogOutClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            DrawerContent(
                onSettingsClick = onSettingsClick,
                onLogOutClick = onLogOutClick
            )
        }
    ) {
        Scaffold(
            topBar = {
                AppBar(
                    title = "Home",
                    onDrawerIconClick = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            },
        ) {
            Column(modifier = modifier.padding(it)) {
                RfidReaderStatusSection(40)
                CategorySection()
            }
        }
    }
}

@Composable
private fun CategorySection() {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Categories",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(DataSource.categories) {
                CategoryItem(category = it, onCategoryItemClick = {

                })
            }
        }
    }
}

@Composable
private fun CategoryItem(
    category: Category, onCategoryItemClick: (CategoryType) -> Unit, modifier: Modifier = Modifier
) {
    Card(
        onClick = { onCategoryItemClick(category.type) },
        shape = RoundedCornerShape(16.dp),
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
                painter = painterResource(id = category.icon),
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
fun RfidReaderStatusSection(
    batteryPercentage: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
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
            Image(painter = painterResource(id = R.drawable.bluetooth), contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(
                    id = BatteryImageUtil.getBatteryImageByPercentage(
                        batteryPercentage
                    )
                ), contentDescription = null, modifier = Modifier.height(24.dp)
            )
        }
    }
}

@Composable
fun DrawerContent(
    onSettingsClick: () -> Unit,
    onLogOutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.7f)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(modifier = modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.drawer_content_upper),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    tint = Color.White,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Administrator",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.account_id_icon),
                            contentDescription = null,
                            modifier = Modifier.scale(1.5f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "123S",
                            color = Color.White
                        )
                    }
                }
            }
        }

        ProfileTextButton(
            text = "Settings",
            onOptionClick = onSettingsClick,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        ProfileTextButton(
            text = "Logout",
            onOptionClick = onLogOutClick,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.weight(1f))
        VersionText(modifier = Modifier.padding(16.dp))
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HomeScreenPreview() {
    HomeScreen(onLogOutClick = { /*TODO*/ }, onSettingsClick = { /*TODO*/ })
}