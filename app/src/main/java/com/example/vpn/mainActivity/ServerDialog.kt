package com.example.vpn.mainActivity


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.vpn.VpnLocation
@Composable
fun ServerDialog(
    locations: List<VpnLocation>,
    isPremiumUser: Boolean,
    onSelect: (VpnLocation) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        containerColor = Color(0xFF0A0F1C),
        title = {
            Text(
                text = "🌐 Choose a Server",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        },
        text = {
            Column {
                if (!isPremiumUser) {
                    Text(
                        text = "🔓 Free plan: only Germany available",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                LazyColumn(modifier = Modifier.height(300.dp)) {
                    items(locations) { location ->
                        val isEnabled = location.isAvailable
                        val textColor = if (isEnabled) Color.White else Color.Gray
                        val bgModifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isEnabled) Color(0xFF1A1F2E) else Color(0xFF2A2E3E))
                            .padding(16.dp)
                            .let {
                                if (isEnabled) it.clickable { onSelect(location) } else it
                            }

                        Column(modifier = bgModifier.padding(vertical = 8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = location.flag,
                                    fontSize = MaterialTheme.typography.headlineSmall.fontSize
                                )
                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${location.city}, ${location.country}",
                                        color = textColor,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = location.ip,
                                        color = Color.Gray,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                SignalIcon(location.signalLevel)

                                if (!isEnabled) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = Color.Gray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    )
}
