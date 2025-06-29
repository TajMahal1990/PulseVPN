package com.example.vpn.mainActivity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp

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
