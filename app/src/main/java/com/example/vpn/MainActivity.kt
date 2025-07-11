package com.example.vpn






import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.vpn.mainActivity.AnimatedConnectionCircle
import com.example.vpn.mainActivity.LocationCard
import com.example.vpn.mainActivity.ServerDialog
import com.wireguard.android.backend.GoBackend
import com.wireguard.android.backend.Tunnel
import com.wireguard.config.Config
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.StringReader



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VPNCard()
            VPNAppWithDrawer()
        }
    }
}

@Composable
fun VPNCard() {
    val context = LocalContext.current
    val vpnServers = listOf(
        VpnLocation("Germany", "Frankfurt", "DE", "🇩🇪", configGermany, 3, true),
        VpnLocation("Singapore", "Singapore", "SG", "🇸🇬", configSingapore, 3, true),
        VpnLocation("France", "Paris", "FR", "🇫🇷", configFrance, 2, false)
    )

    var selectedServer by remember { mutableStateOf(vpnServers[0]) }
    var showDialog by remember { mutableStateOf(false) }
    var isConnected by remember { mutableStateOf(false) }
    var vpnError by remember { mutableStateOf<String?>(null) }
    val connectedTime = remember { mutableStateOf("00:00") }
    val upload = remember { mutableStateOf("0 KB/s") }
    val download = remember { mutableStateOf("0 KB/s") }

    val backend = remember { GoBackend(context) }
    val tunnel = remember {
        object : Tunnel {
            override fun getName() = selectedServer.city
            override fun onStateChange(newState: Tunnel.State) {}
        }
    }

    val vpnPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            launchVpn(backend, tunnel, selectedServer.config) {
                isConnected = it
                vpnError = if (it) null else "Ошибка при запуске VPN"
            }
        } else {
            vpnError = "Разрешение на VPN отклонено"
        }
    }

    LaunchedEffect(isConnected) {
        if (isConnected) {
            var lastRx = 0L
            var lastTx = 0L
            var seconds = 0

            while (isConnected) {
                try {
                    val stats = withContext(Dispatchers.IO) { backend.getStatistics(tunnel) }
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
        AnimatedConnectionCircle(isConnected = isConnected, onToggle = {
            if (!isConnected) {
                val intent = GoBackend.VpnService.prepare(context)
                if (intent != null) {
                    vpnPermissionLauncher.launch(intent)
                } else {
                    launchVpn(backend, tunnel, selectedServer.config) {
                        isConnected = it
                        vpnError = if (it) null else "Ошибка при запуске VPN"
                    }
                }
            } else {
                stopVpn(
                    backend, tunnel,
                    onSuccess = { isConnected = false },
                    onError = { vpnError = it }
                )
            }
        })
        Spacer(modifier = Modifier.height(24.dp))
        LocationCard(selectedServer)
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
                .background(Brush.horizontalGradient(listOf(Color(0xFF00FFC8), Color(0xFF0078A0))))
                .clickable { showDialog = true },
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Change Server", style = MaterialTheme.typography.labelLarge, color = Color.Black)
            }
        }
        if (showDialog) {
            ServerDialog(vpnServers, onSelect = {
                selectedServer = it
                showDialog = false
            }, onDismiss = { showDialog = false })
        }
        vpnError?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}

data class VpnLocation(
    val country: String,
    val city: String,
    val code: String,
    val flag: String,
    val config: String,
    val signalLevel: Int,
    val isAvailable: Boolean
)

val configGermany = """[Interface]
PrivateKey = eMTgL1HBd3TC/GHSOhCDFyPHlyA/4KjmftZNwAI9dVI=
Address = 10.66.66.2/32,fd42:42:42::2/128
DNS = 1.1.1.1,1.0.0.1

[Peer]
PublicKey = evSSRsdVYG3D4SI/ANbEj86R1hz3bgG+evzwBl+ce1A=
PresharedKey = 9LLvDv0QOQ52zDy+UGlr4dGPghLaTrGWCY6Wg7ZaCK0=
Endpoint = 79.133.46.112:56258
AllowedIPs = 0.0.0.0/0,::/0""".trimIndent()

val configSingapore = """[Interface]
PrivateKey = ...
Address = ...
DNS = 1.1.1.1

[Peer]
PublicKey = ...
Endpoint = ...
AllowedIPs = 0.0.0.0/0,::/0""".trimIndent()

val configFrance = """[Interface]
PrivateKey = ...
Address = ...
DNS = 1.1.1.1

[Peer]
PublicKey = ...
Endpoint = ...
AllowedIPs = 0.0.0.0/0,::/0""".trimIndent()

fun launchVpn(backend: GoBackend, tunnel: Tunnel, configString: String, onResult: (Boolean) -> Unit) {
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

fun stopVpn(backend: GoBackend, tunnel: Tunnel, onSuccess: () -> Unit, onError: (String) -> Unit) {
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
