package com.example.vpn.mainActivity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp



@Composable
fun VPNCard() {
    val allLocations = getDefaultLocations()
    var selectedLocation by remember { mutableStateOf(allLocations.first()) }
    var showDialog by remember { mutableStateOf(false) }
    var isConnected by remember { mutableStateOf(false) }
    val connectedTime = remember { mutableStateOf("03:42") }
    val upload = remember { mutableStateOf("28,3 MB") }
    val download = remember { mutableStateOf("19,4 MB") }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        AnimatedConnectionCircle(
            isConnected = isConnected,
            onToggle = { isConnected = !isConnected }
        )
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
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.Black)
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
