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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ServerDialog(
    locations: List<VpnLocation>,
    onSelect: (VpnLocation) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = {
            Text(
                "Choose Server",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        },
        containerColor = Color(0xFF1A1F2E),
        text = {
            Column {
                Text(
                    "Free plan: only Germany & Netherlands available",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
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
                            Text(
                                "${location.city}, ${location.country}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = textColor
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            SignalIcon(location.signalLevel)
                            if (!isEnabled) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}
