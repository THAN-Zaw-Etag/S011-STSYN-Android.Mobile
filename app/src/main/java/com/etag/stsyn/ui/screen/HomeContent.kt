@file:OptIn(ExperimentalMaterial3Api::class)

package com.etag.stsyn.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.etag.stsyn.R
import com.etag.stsyn.ui.components.AppBar
import com.etag.stsyn.ui.components.BottomNavigationBar
import com.etag.stsyn.ui.components.ProfileTextButton
import com.etag.stsyn.ui.components.VersionText
import com.etag.stsyn.ui.navigation.HomeNavigationGraph
import com.etag.stsyn.ui.viewmodel.SharedUiViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeContent(
    onLogOutClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val navController = rememberNavController()
    val sharedUiViewModel: SharedUiViewModel = hiltViewModel()
    val sharedUiState by sharedUiViewModel.uiState.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = modifier,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            DrawerContent(
                onSettingsClick = onSettingsClick,
                onLogOutClick = onLogOutClick
            )
        }
    ) {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    selectedItem = sharedUiState.selectedBottomNavigationItem,
                    showBottomBar = sharedUiState.showBottomNavigationBar,
                    onBottomNavigationItemSelected = { navController.navigate(it) })
            },
            topBar = {
                AppBar(
                    title = sharedUiState.title,
                    onDrawerIconClick = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            },
        ) {
            HomeNavigationGraph(
                navController = navController,
                sharedUiViewModel = sharedUiViewModel,
                modifier = Modifier.padding(it)
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