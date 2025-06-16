package com.example.vpn



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import android.app.Activity
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VPNApp()
        }
    }
}

@Composable
fun VPNApp() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("VPN", "Account", "Settings")

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF0A0F1C)) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.AccountBox, contentDescription = null) },
                    label = { Text("VPN") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Account") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("Settings") }
                )
            }
        },
        containerColor = Color(0xFF0A0F1C)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            if (selectedTab == 0) {
                VPNCard()
            } else if (selectedTab == 1) {
                AccountScreen()
            } else {
                // SettingsScreen()
            }
        }
    }
}

@Composable
fun VPNCard() {
    val allLocations = listOf(
        VpnLocation("Germany", "Frankfurt", "DE", 3, true),
        VpnLocation("Netherlands", "Amsterdam", "NL", 3, true),
        VpnLocation("USA", "New York", "US", 2, false),
        VpnLocation("France", "Paris", "FR", 1, false),
        VpnLocation("UK", "London", "GB", 2, false)
    )

    var selectedLocation by remember { mutableStateOf(allLocations.first()) }
    var showDialog by remember { mutableStateOf(false) }

    val connectedTime = remember { mutableStateOf("03:42") }
    val upload = remember { mutableStateOf("28,3 MB") }
    val download = remember { mutableStateOf("19,4 MB") }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text("VPN LOGO", style = MaterialTheme.typography.headlineLarge, color = Color.White)
        Spacer(modifier = Modifier.height(32.dp))

        Text("You are protected", style = MaterialTheme.typography.titleLarge, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF00FFC8), modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(24.dp))
        AnimatedConnectionCircle()
        Spacer(modifier = Modifier.height(24.dp))
        LocationCard(selectedLocation)
        Spacer(modifier = Modifier.height(24.dp))
        Text("↑ ${upload.value}   ↓ ${download.value}", style = MaterialTheme.typography.bodyLarge, color = Color(0xFFB0BEC5))
        Spacer(modifier = Modifier.height(4.dp))
        Text("Connected: ${connectedTime.value}", style = MaterialTheme.typography.bodySmall, color = Color(0xFFB0BEC5))
        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .height(48.dp)
                .width(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF00FFC8), Color(0xFF0078A0))
                    )
                )
                .clickable { showDialog = true },
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Change Server",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Black
                )
            }
        }

        if (showDialog) {
            ServerDialog(allLocations, onSelect = {
                selectedLocation = it
                showDialog = false
            }, onDismiss = { showDialog = false })
        }
    }
}

@Composable
fun ServerDialog(locations: List<VpnLocation>, onSelect: (VpnLocation) -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = {
            Text("Choose Server", style = MaterialTheme.typography.titleMedium, color = Color.White)
        },
        containerColor = Color(0xFF1A1F2E),
        text = {
            Column {
                Text("Free plan: only Germany & Netherlands available", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.height(250.dp)) {
                    items(locations) { location ->
                        val isEnabled = location.isAvailable
                        val bgColor = if (isEnabled) Color.Transparent else Color(0xFF2A2E3E)
                        val textColor = if (isEnabled) Color.White else Color.Gray

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(bgColor)
                                .padding(12.dp)
                                .clickable(enabled = isEnabled) { onSelect(location) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FlagEmoji(location.code)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("${location.city}, ${location.country}", style = MaterialTheme.typography.bodyMedium, color = textColor)
                            Spacer(modifier = Modifier.weight(1f))
                            SignalIcon(location.signalLevel)
                            if (!isEnabled) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun AnimatedConnectionCircle() {
    val infiniteTransition = rememberInfiniteTransition()
    val waveAlpha by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse)
    )
    val waveScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse)
    )

    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier.size(280.dp).graphicsLayer {
                scaleX = waveScale
                scaleY = waveScale
                alpha = waveAlpha
            }.background(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF00FFC8), Color.Transparent),
                    radius = 400f
                ), shape = CircleShape
            )
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(220.dp).clip(CircleShape).border(2.dp, Color(0xFF00FFC8), CircleShape)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("CONNECTED", color = Color.White, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier.height(48.dp).width(160.dp).clip(RoundedCornerShape(12.dp)).background(
                        brush = Brush.horizontalGradient(listOf(Color(0xFF00FFC8), Color.White))
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("DISCONNECT", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun LocationCard(location: VpnLocation) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color(0xFF11182A)).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FlagEmoji(location.code)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text("${location.city}, ${location.country}", color = Color.White, fontSize = 14.sp)
            Text("IP: 145.66.32.1", color = Color(0xFFB0BEC5), fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF00FFC8), modifier = Modifier.size(24.dp))
    }
}

@Composable
fun SignalIcon(level: Int) {
    val bars = 3
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        for (i in 1..bars) {
            Box(
                modifier = Modifier
                    .size(width = 4.dp, height = (6 + i * 4).dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(if (i <= level) Color(0xFF00FFC8) else Color.Gray)
            )
        }
    }
}

@Composable
fun FlagEmoji(countryCode: String) {
    val emojiFlag = countryCode
        .uppercase()
        .map { 0x1F1E6 - 'A'.code + it.code }
        .map { Character.toChars(it).concatToString() }
        .joinToString("")

    Text(emojiFlag, fontSize = 24.sp)
}

data class VpnLocation(
    val country: String,
    val city: String,
    val code: String,
    val signalLevel: Int,
    val isAvailable: Boolean
)