package com.example.vpn.mainActivity

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.vpn.R
import kotlinx.coroutines.launch

@Composable
fun AnimatedConnectionCircle(
    isConnected: Boolean,
    onToggle: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    /* ───────── ВОЛНЫ ───────── */

    val waveTransition = rememberInfiniteTransition(label = "waveTransition")

    val wave1Scale by waveTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ), label = "wave1Scale"
    )

    val wave1Alpha by waveTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "wave1Alpha"
    )

    val wave2Scale by waveTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, delayMillis = 1300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ), label = "wave2Scale"
    )

    val wave2Alpha by waveTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, delayMillis = 1300, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "wave2Alpha"
    )

    /* ───────── ВЗЛЁТ И FLASH ───────── */

    val liftAnim = remember { Animatable(0f) }
    val rotateAnim = remember { Animatable(0f) }
    val flashAnim = remember { Animatable(0f) }

    LaunchedEffect(isConnected) {
        if (isConnected) {
            launch {
                liftAnim.snapTo(0f)
                liftAnim.animateTo(-40f, tween(700, easing = FastOutSlowInEasing))
                liftAnim.animateTo(0f, tween(500))
            }
            launch {
                rotateAnim.snapTo(0f)
                rotateAnim.animateTo(360f, tween(1200))
                rotateAnim.snapTo(0f)
            }
            launch {
                flashAnim.snapTo(0f)
                flashAnim.animateTo(1f, tween(400))
                flashAnim.animateTo(0f, tween(300))
            }
        }
    }

    /* ───────── ЦВЕТА ───────── */

    val pulseColor = if (isConnected) Color(0xFF00FFC8) else Color(0xFF7375FF)

    val coreGradient = if (isConnected) {
        Brush.horizontalGradient(
            listOf(Color.White, Color(0xFF66FFF5)) // теперь для включённого
        )
    } else {
        Brush.horizontalGradient(
            listOf(Color(0xFF00FFC8), Color(0xFF0078A0)) // теперь для выключенного
        )
    }


    /* ───────── UI ───────── */

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.Center
    ) {
        // мягкое радиальное свечение (всегда)
        Box(
            modifier = Modifier
                .size(280.dp)
                .background(
                    Brush.radialGradient(
                        listOf(pulseColor.copy(0.20f), Color.Transparent),
                        radius = 350f
                    ),
                    CircleShape
                )
                .align(Alignment.Center)
        )

        if (isConnected) {
            // Первая волна
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .graphicsLayer {
                        scaleX = wave1Scale
                        scaleY = wave1Scale
                        alpha = wave1Alpha
                    }
                    .background(pulseColor, CircleShape)
                    .align(Alignment.Center)
            )

            // Вторая волна
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .graphicsLayer {
                        scaleX = wave2Scale
                        scaleY = wave2Scale
                        alpha = wave2Alpha
                    }
                    .background(pulseColor, CircleShape)
                    .align(Alignment.Center)
            )

            // Flash-кольцо
            if (flashAnim.value > 0f) {
                val flashSize = 120f + 120f * flashAnim.value
                Box(
                    modifier = Modifier
                        .size(flashSize.dp)
                        .graphicsLayer { alpha = 1f - flashAnim.value }
                        .background(pulseColor, CircleShape)
                        .align(Alignment.Center)
                )
            }
        }

        // Центральная кнопка
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(coreGradient)
                .clickable {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onToggle()
                }
                .align(Alignment.Center)
        ) {
            Icon(
                painterResource(R.drawable.baseline_rocket_launch_24),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .size(48.dp)
                    .offset(y = liftAnim.value.dp)
                    .rotate(rotateAnim.value)
            )
        }
    }
}