package com.example.vpn




import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VPNAppWithDrawer() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedTab by remember { mutableStateOf(0) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFF1A1F2E),
                modifier = Modifier.width(280.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "PULSE VPN",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    Divider(color = Color(0xFF444A65), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    DrawerItem(
                        label = "VPN",
                        icon = Icons.Default.Lock,
                        selected = selectedTab == 0,
                        onClick = {
                            selectedTab = 0
                            scope.launch { drawerState.close() }
                        }
                    )
                    DrawerItem(
                        label = "Premium-функция",
                        icon = Icons.Default.Star,
                        selected = selectedTab == 1,
                        onClick = {
                            selectedTab = 1
                            scope.launch { drawerState.close() }
                        }
                    )
                    DrawerItem(
                        label = "Настройки",
                        icon = Icons.Default.Settings,
                        selected = selectedTab == 2,
                        onClick = {
                            selectedTab = 2
                            scope.launch { drawerState.close() }
                        }
                    )
                    DrawerItem(
                        label = "Отзывы и поддержка",
                        icon = Icons.Default.Chat,
                        selected = selectedTab == 3,
                        onClick = {
                            selectedTab = 3
                            scope.launch { drawerState.close() }
                        }
                    )
                    DrawerItem(
                        label = "Оцените приложение",
                        icon = Icons.Default.ThumbUp,
                        selected = selectedTab == 4,
                        onClick = {
                            selectedTab = 4
                            scope.launch { drawerState.close() }
                        }
                    )
                    DrawerItem(
                        label = "Фильтр",
                        icon = Icons.Default.Tune,
                        selected = selectedTab == 5,
                        onClick = {
                            selectedTab = 5
                            scope.launch { drawerState.close() }
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    Text("v1.0.0", color = Color.Gray, modifier = Modifier.padding(16.dp))
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("PULSE", color = Color.White, fontWeight = FontWeight.Bold)
                                Text("VPN", color = Color(0xFF00FFC8), fontWeight = FontWeight.Bold)
                            }
                        }
                    },
                    navigationIcon = {
                        if (selectedTab == 0) {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Меню",
                                    tint = Color.White
                                )
                            }
                        } else {
                            IconButton(onClick = { selectedTab = 0 }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Назад",
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    actions = {
                        Spacer(modifier = Modifier.width(48.dp))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF0A0F1C),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            }
            ,
            containerColor = Color(0xFF0A0F1C)
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                when (selectedTab) {
                    0 -> VPNCard()
                    1 -> AccountScreen()
                    2 -> SettingsScreen()
                    3 -> SupportScreen()
                    4 -> RateAppScreen()
                    5 -> FilterScreen()
                }
            }
        }
    }
}

@Composable
fun DrawerItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(
                text = label,
                color = if (selected) Color(0xFF00FFC8) else Color.White,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        },
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) Color(0xFF00FFC8) else Color.White
            )
        },
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(8.dp))
    )
}

