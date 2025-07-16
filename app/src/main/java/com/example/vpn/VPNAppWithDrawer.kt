package com.example.vpn



import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

private val PulseGradient = Brush.horizontalGradient(
    listOf(Color(0xFF00FFC8), Color(0xFF0078A0))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VPNAppWithDrawer(isPremiumUser: Boolean) {
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ASTRO",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "VPN",
                            color = Color(0xFF00FFC8),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Divider(color = Color(0xFF444A65), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    DrawerItem("VPN", Icons.Default.Lock, selectedTab == 0) {
                        selectedTab = 0; scope.launch { drawerState.close() }
                    }
                    DrawerItem("Premium-функция", Icons.Default.Star, selectedTab == 1) {
                        selectedTab = 1; scope.launch { drawerState.close() }
                    }
                    DrawerItem("Настройки", Icons.Default.Settings, selectedTab == 2) {
                        selectedTab = 2; scope.launch { drawerState.close() }
                    }
                    DrawerItem("Отзывы и поддержка", Icons.Default.Chat, selectedTab == 3) {
                        selectedTab = 3; scope.launch { drawerState.close() }
                    }
                    DrawerItem("Оцените приложение", Icons.Default.ThumbUp, selectedTab == 4) {
                        selectedTab = 4; scope.launch { drawerState.close() }
                    }
                    DrawerItem("Фильтр", Icons.Default.Tune, selectedTab == 5) {
                        selectedTab = 5; scope.launch { drawerState.close() }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "v1.0.0",
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("ASTRO", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("VPN", color = Color(0xFF00FFC8), fontWeight = FontWeight.Bold)
                            Spacer(Modifier.width(8.dp))

                            if (isPremiumUser) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Premium",
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
                                Text(
                                    text = "FREE",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFF444A65))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                if (selectedTab == 0) scope.launch { drawerState.open() }
                                else selectedTab = 0
                            }
                        ) {
                            Icon(
                                imageVector = if (selectedTab == 0) Icons.Default.Menu else Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        Spacer(modifier = Modifier.width(48.dp)) // справа для симметрии
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF0A0F1C),
                        titleContentColor = Color.White
                    )
                )
            },
            containerColor = Color(0xFF0A0F1C)
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFF1A1F2E), Color(0xFF0A0F1C)),
                            radius = 1000f
                        )
                    )
            ) {
                NeonParticles()

                when (selectedTab) {
                    0 -> VPNCard(isPremiumUser = isPremiumUser)
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
    val baseModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 6.dp)
        .clip(RoundedCornerShape(12.dp))

    val modifier = if (selected) {
        baseModifier.background(PulseGradient)
    } else {
        baseModifier.background(Color.Transparent)
    }

    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) Color.Black else Color.White
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                color = if (selected) Color.Black else Color.White,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
@Composable
fun NeonParticles() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        for (i in 1..40) {
            val x = (0..size.width.toInt()).random().toFloat()
            val y = (0..size.height.toInt()).random().toFloat()
            val radius = (1..6).random().toFloat()

            drawCircle(
                color = Color(0xFF00FFC8).copy(alpha = 0.08f),
                radius = radius,
                center = Offset(x, y)
            )
        }
    }
}
