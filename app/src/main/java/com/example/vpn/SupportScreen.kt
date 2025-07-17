package com.example.vpn
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.clip

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SupportScreen(isPremiumUser: Boolean) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0F1C))
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Поддержка",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (isPremiumUser) {
            // 🔥 Премиум-поддержка
            SupportCard(
                title = "Премиум",
                email = "support@pulsevpn.app",
                telegramLink = "https://t.me/pulsevpn_bot"
            )
        } else {
            // 💡 Базовая поддержка
            SupportCard(
                title = "Базовая",
                email = "support@pulsevpn.app"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Поддержка в Telegram доступна только премиум-пользователям.",
                fontSize = 13.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun SupportCard(
    title: String,
    email: String,
    telegramLink: String? = null
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1A1F2E))
            .padding(16.dp)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("📧 Email:", color = Color(0xFFB0BEC5), fontSize = 14.sp)
        Text(email, color = Color.White, fontSize = 14.sp)

        if (telegramLink != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text("🤖 Telegram-менеджер:", color = Color(0xFFB0BEC5), fontSize = 14.sp)

            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(telegramLink))
                    context.startActivity(intent)
                },
                modifier = Modifier.padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FFC8))
            ) {
                Text("Открыть Telegram-бота", color = Color.Black)
            }
        }
    }
}
