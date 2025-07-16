package com.example.vpn






import androidx.compose.runtime.saveable.rememberSaveable

import com.example.vpn.Premium.FreePlanLimiter

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
import androidx.compose.runtime.saveable.rememberSaveable
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
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.StringReader
import java.util.concurrent.TimeUnit



/**
 * Re-organised version splitting FREE vs PREMIUM logic.
 * FREE tier: one server (Germany), 200 KB/s cap, 15 min/day connection budget.
 * Premium: no limits.
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isPremiumUser by rememberSaveable { mutableStateOf(false) }
            VPNAppWithDrawer(isPremiumUser = isPremiumUser)
        }
    }
}

@Composable
fun VPNCard(isPremiumUser: Boolean) {
    val context = LocalContext.current

    val allServers = listOf(
        VpnLocation(
            "Germany", "Frankfurt", "DE", "🇩🇪",
            configGermany, 3, true,
            ip = extractIpFromConfig(configGermany)
        ),
        VpnLocation(
            "Singapore", "Singapore", "SG", "🇸🇬",
            configSingapore, 3, isPremiumUser,
            ip = extractIpFromConfig(configSingapore)
        ),
        VpnLocation(
            "France", "Paris", "FR", "🇫🇷",
            configFrance, 2, isPremiumUser,
            ip = extractIpFromConfig(configFrance)
        ),
        VpnLocation(
            "Netherlands", "Amsterdam", "NL", "🇳🇱",
            configNetherlands, 3, isPremiumUser,
            ip = extractIpFromConfig(configNetherlands)
        ),
        VpnLocation(
            "Switzerland", "Zurich", "CH", "🇨🇭",
            configSwitzerland, 3, isPremiumUser,
            ip = extractIpFromConfig(configSwitzerland)
        )
    )


    val servers = allServers.map { server ->
        val isAvailable = isPremiumUser || server.country == "Germany"
        server.copy(isAvailable = isAvailable)
    }


    var selected by remember { mutableStateOf(servers[0]) }
    var showDialog by remember { mutableStateOf(false) }
    var isConnected by remember { mutableStateOf(false) }
    var err by remember { mutableStateOf<String?>(null) }

    val up = remember { mutableStateOf("0 KB/s") }
    val down = remember { mutableStateOf("0 KB/s") }
    val duration = remember { mutableStateOf("00:00") }

    val limiter = remember { FreePlanLimiter(context) }
    val backend = remember { GoBackend(context) }

    val tunnel = remember {
        object : Tunnel {
            override fun getName() = selected.city
            override fun onStateChange(newState: Tunnel.State) {}
        }
    }

    val vpnPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { res ->
        if (res.resultCode == Activity.RESULT_OK) {
            startVpn(backend, tunnel, selected.config) {
                isConnected = it
                if (!it) err = "Failed to start VPN"
            }
        } else err = "VPN permission denied"
    }

    LaunchedEffect(isConnected) {
        if (isConnected) {
            var lastRx = 0L; var lastTx = 0L; var sec = 0
            while (isConnected) {
                if (!isPremiumUser && !limiter.tick()) {
                    stopVpn(backend, tunnel) { isConnected = false }; break
                }
                val stats = withContext(Dispatchers.IO) { backend.getStatistics(tunnel) }
                val rx = stats?.totalRx() ?: 0L
                val tx = stats?.totalTx() ?: 0L
                val cap = if (isPremiumUser) Long.MAX_VALUE else 200 * 1024L
                down.value = formatSpeed((rx - lastRx).coerceAtMost(cap))
                up.value = formatSpeed((tx - lastTx).coerceAtMost(cap))
                lastRx = rx; lastTx = tx
                duration.value = formatDuration(++sec)
                delay(1000)
            }
        } else {
            duration.value = "00:00"; up.value = "0 KB/s"; down.value = "0 KB/s"
        }
    }

    Column(
        Modifier.fillMaxSize().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))

        AnimatedConnectionCircle(isConnected) {
            if (isConnected) stopVpn(backend, tunnel) { isConnected = false }
            else if (!isPremiumUser && limiter.remaining() <= 0) {
                err = "Free daily limit used up"
            } else {
                GoBackend.VpnService.prepare(context)?.let(vpnPermissionLauncher::launch)
                    ?: startVpn(backend, tunnel, selected.config) {
                        isConnected = it
                        if (!it) err = "Failed to start VPN"
                    }
            }
        }

        Spacer(Modifier.height(24.dp))
        LocationCard(location = selected, onClick = { showDialog = true })
        Spacer(Modifier.height(16.dp))

        Text("↑ ${up.value}   ↓ ${down.value}", style = MaterialTheme.typography.bodyLarge, color = Color(0xFFB0BEC5))
        Text("Connected: ${duration.value}", style = MaterialTheme.typography.bodySmall, color = Color(0xFFB0BEC5))

        if (!isPremiumUser) {
            Spacer(Modifier.height(16.dp))
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFF2A2E3E)).padding(12.dp)
            ) {
                Text("🔒 Free plan enabled", color = Color(0xFFFFA726), style = MaterialTheme.typography.bodyLarge)
                Text("Germany only · 200 KB/s · 15 min/day", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⏳ Remaining time: ", color = Color.White, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        formatDuration(limiter.remaining()),
                        color = if (limiter.remaining() <= 60) Color.Red else Color(0xFF00FFC8),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        if (showDialog) {
            ServerDialog(
                locations = servers,
                isPremiumUser = isPremiumUser, // ← вот этого не хватало
                onSelect = { selected = it; showDialog = false },
                onDismiss = { showDialog = false }
            )
        }


        err?.let {
            Spacer(Modifier.height(12.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}



/* =============== HELPERS =============== */

data class VpnLocation(
    val country: String,
    val city: String,
    val code: String,
    val flag: String,
    val config: String,
    val signalLevel: Int,
    val isAvailable: Boolean,
    val ip: String )

val configGermany = """[Interface]
PrivateKey = eMTgL1HBd3TC/GHSOhCDFyPHlyA/4KjmftZNwAI9dVI=
Address = 10.66.66.2/32,fd42:42:42::2/128
DNS = 1.1.1.1,1.0.0.1

[Peer]
PublicKey = evSSRsdVYG3D4SI/ANbEj86R1hz3bgG+evzwBl+ce1A=
PresharedKey = 9LLvDv0QOQ52zDy+UGlr4dGPghLaTrGWCY6Wg7ZaCK0=
Endpoint = 79.133.46.112:56258
AllowedIPs = 0.0.0.0/0,::/0""".trimIndent()

val configSingapore = """[Interface]\nPrivateKey=...\nAddress=...\nDNS=1.1.1.1\n[Peer]\nPublicKey=...\nEndpoint=...\nAllowedIPs=0.0.0.0/0,::/0"""
val configFrance = configSingapore

val configNetherlands = """[Interface]
PrivateKey = ...
Address = ...
DNS = 1.1.1.1

[Peer]
PublicKey = ...
Endpoint = 95.179.220.55:51820
AllowedIPs = 0.0.0.0/0,::/0""".trimIndent()

val configSwitzerland = """[Interface]
PrivateKey = ...
Address = ...
DNS = 1.1.1.1

[Peer]
PublicKey = ...
Endpoint = 185.104.185.59:51820
AllowedIPs = 0.0.0.0/0,::/0""".trimIndent()

fun startVpn(backend: GoBackend, tunnel: Tunnel, cfg: String, cb: (Boolean) -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        try {
            val config = withContext(Dispatchers.IO) { Config.parse(BufferedReader(StringReader(cfg))) }
            withContext(Dispatchers.IO) { backend.setState(tunnel, Tunnel.State.UP, config) }
            cb(true)
        } catch (e: Exception) { cb(false) }
    }
}

fun stopVpn(backend: GoBackend, tunnel: Tunnel, cb: () -> Unit) = try {
    backend.setState(tunnel, Tunnel.State.DOWN, null); cb()
} catch (e: Exception) {}

fun formatSpeed(bps: Long): String {
    val kb = bps / 1024.0; val mb = kb / 1024.0
    return when { mb >= 1 -> "%.1f MB/s".format(mb); kb >= 1 -> "%.1f KB/s".format(kb); else -> "$bps B/s" }
}

fun formatDuration(s: Int): String = "%02d:%02d".format(s / 60, s % 60)


fun extractIpFromConfig(config: String): String {
    val endpointLine = config.lines().find { it.trim().startsWith("Endpoint") }
    return endpointLine?.substringAfter("=")?.substringBefore(":")?.trim() ?: "N/A"
}
