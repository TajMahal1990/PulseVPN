package com.example.vpn.mainActivity

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.wireguard.android.backend.GoBackend
import com.wireguard.android.backend.Tunnel
import com.wireguard.config.Config
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.StringReader

@Composable
fun VPNCard() {
    val context = LocalContext.current
    val allLocations = getDefaultLocations()
    var selectedLocation by remember { mutableStateOf(allLocations.first()) }
    var showDialog by remember { mutableStateOf(false) }
    var isConnected by remember { mutableStateOf(false) }
    val connectedTime = remember { mutableStateOf("00:00") }
    val upload = remember { mutableStateOf("0 KB/s") }
    val download = remember { mutableStateOf("0 KB/s") }

    val backend = remember { GoBackend(context) }
    val tunnel = remember {
        object : Tunnel {
            override fun getName() = selectedLocation.city
            override fun onStateChange(newState: Tunnel.State) {}
        }
    }

    val handleToggle = {
        if (!isConnected) {
            val intent = GoBackend.VpnService.prepare(context)
            if (intent != null) {
                context.startActivity(intent)
            } else {
                launchVpn(backend, tunnel, getConfigFor(selectedLocation)) {
                    isConnected = it
                }
            }
        } else {
            stopVpn(backend, tunnel,
                onSuccess = { isConnected = false },
                onError = {})
        }
    }

    LaunchedEffect(isConnected) {
        if (isConnected) {
            var lastRx = 0L
            var lastTx = 0L
            var seconds = 0

            while (isConnected) {
                try {
                    val stats = withContext(Dispatchers.IO) {
                        backend.getStatistics(tunnel)
                    }
                    val rx = stats?.totalRx() ?: 0L
                    val tx = stats?.totalTx() ?: 0L
                    download.value = formatSpeed(rx - lastRx)
                    upload.value = formatSpeed(tx - lastTx)
                    lastRx = rx
                    lastTx = tx
                    connectedTime.value = formatDuration(seconds++)
                } catch (_: Exception) {}
                delay(1000)
            }
            connectedTime.value = "00:00"
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        AnimatedConnectionCircle(isConnected = isConnected, onToggle = handleToggle)
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

fun launchVpn(
    backend: GoBackend,
    tunnel: Tunnel,
    configString: String,
    onResult: (Boolean) -> Unit
) {
    CoroutineScope(Dispatchers.Main).launch {
        delay(100)
        try {
            val config = withContext(Dispatchers.IO) {
                Config.parse(BufferedReader(StringReader(configString)))
            }
            withContext(Dispatchers.IO) {
                backend.setState(tunnel, Tunnel.State.UP, config)
            }
            onResult(true)
        } catch (e: Exception) {
            onResult(false)
        }
    }
}

fun stopVpn(
    backend: GoBackend,
    tunnel: Tunnel,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    try {
        backend.setState(tunnel, Tunnel.State.DOWN, null)
        onSuccess()
    } catch (e: Exception) {
        onError(e.message ?: "неизвестно")
    }
}

fun formatSpeed(bytesPerSec: Long): String {
    val kb = bytesPerSec / 1024.0
    val mb = kb / 1024.0
    return when {
        mb >= 1 -> "%.1f MB/s".format(mb)
        kb >= 1 -> "%.1f KB/s".format(kb)
        else -> "$bytesPerSec B/s"
    }
}

fun formatDuration(seconds: Int): String = "%02d:%02d".format(seconds / 60, seconds % 60)

fun getConfigFor(location: VpnLocation): String {
    return when (location.code) {
        "DE" -> """[Interface]\nPrivateKey = ...\nAddress = ...\nDNS = 1.1.1.1\n\n[Peer]\nPublicKey = ...\nEndpoint = 79.133.46.112:56258\nAllowedIPs = 0.0.0.0/0,::/0"""
        else -> """[Interface]\nPrivateKey = ...\nAddress = ...\nDNS = 1.1.1.1\n\n[Peer]\nPublicKey = ...\nEndpoint = ...\nAllowedIPs = 0.0.0.0/0,::/0"""
    }
}
