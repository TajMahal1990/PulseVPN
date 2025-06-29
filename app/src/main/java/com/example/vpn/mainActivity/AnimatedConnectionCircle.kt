package com.example.vpn.mainActivity
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send // или другую иконку, если нужен rocket — подключи SVG/painter
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.animation.animateColorAsState
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.example.vpn.R

@Composable
fun AnimatedConnectionCircle(
    isConnected: Boolean,
    onToggle: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "circle-pulse")

    // Плавные волны (2 штуки, сдвинуты по времени)
    val wave1 by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            tween(1800, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = "wave1"
    )
    val wave2 by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            tween(1800, delayMillis = 600, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = "wave2"
    )

    val waveAlpha1 by infiniteTransition.animateFloat(
        initialValue = if (isConnected) 0.22f else 0.13f,
        targetValue = 0.02f,
        animationSpec = infiniteRepeatable(
            tween(1800, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = "waveAlpha1"
    )
    val waveAlpha2 by infiniteTransition.animateFloat(
        initialValue = if (isConnected) 0.22f else 0.13f,
        targetValue = 0.02f,
        animationSpec = infiniteRepeatable(
            tween(1800, delayMillis = 600, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = "waveAlpha2"
    )

    // Цвет кнопки меняется плавно (анимированно)
    val buttonColor by animateColorAsState(
        targetValue = if (isConnected) Color(0xFF00FFC8) else Color(0xFF7375FF),
        animationSpec = tween(500)
    )
    val buttonGradient = if (isConnected)
        Brush.horizontalGradient(listOf(Color(0xFF00FFC8), Color(0xFF0078A0)))
    else
        Brush.horizontalGradient(listOf(Color(0xFF9084FF), Color(0xFF7375FF)))

    Box(contentAlignment = Alignment.Center) {
        // Radiant BG
        Box(
            modifier = Modifier
                .size(280.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            if (isConnected) Color(0xFF00FFC8).copy(alpha = 0.20f)
                            else Color(0xFF9084FF).copy(alpha = 0.15f),
                            Color.Transparent
                        ),
                        radius = 350f
                    ),
                    shape = CircleShape
                )
        )
        // ВОЛНЫ
        Box(
            modifier = Modifier
                .size((160 * wave1).dp)
                .graphicsLayer { alpha = waveAlpha1 }
                .background(buttonColor, shape = CircleShape)
        )
        Box(
            modifier = Modifier
                .size((160 * wave2).dp)
                .graphicsLayer { alpha = waveAlpha2 }
                .background(buttonColor, shape = CircleShape)
        )

        val buttonGradient = if (isConnected)
            Brush.horizontalGradient(listOf(Color(0xFF66FFF5), Color(0xFF0078A0)))
        else
            Brush.horizontalGradient(listOf(Color.White, Color(0xFF66FFF5)))

        val borderColor = if (isConnected) Color(0xFF00FFC8) else Color(0xFF66FFF5)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(buttonGradient)
                .clickable { onToggle() }
                .border(3.dp, borderColor, CircleShape)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_rocket_launch_24), // или shield
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(48.dp)
            )
        }

    }

}
