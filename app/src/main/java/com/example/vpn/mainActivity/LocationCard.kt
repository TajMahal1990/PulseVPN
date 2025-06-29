package com.example.vpn.mainActivity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


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
