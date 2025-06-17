package com.example.vpn

import android.app.Activity
import androidx.compose.ui.platform.LocalContext

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource

import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock

import androidx.compose.ui.draw.clip

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ConsentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val agreed = prefs.getBoolean("consent_given", false)

        if (agreed) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        setContent {
            // ✅ Принудительно задаём тёмную тему, как в MainActivity
            MaterialTheme(
                colorScheme = darkColorScheme(
                    background = Color(0xFF0A0F1C),
                    surface = Color(0xFF0A0F1C),
                    onBackground = Color.White,
                    onSurface = Color.White,
                    primary = Color(0xFF00FFC8)
                )
            ) {
                ConsentScreen {
                    prefs.edit().putBoolean("consent_given", true).apply()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}
@Composable
fun ConsentScreen(onAgree: () -> Unit) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0F1C))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    // 🔙 Назад
                    Spacer(modifier = Modifier.height(48.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDialog = true },
                        contentAlignment = Alignment.TopStart
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 🔷 Лого
                    Image(
                        painter = painterResource(id = R.drawable.vpnlogo__1_),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 🟢 Заголовок
                    Text(
                        text = "Добро пожаловать в PulseVPN",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 📘 Строка 1: Политика (annotated)
                    val policyText = buildAnnotatedString {
                        append("Прочтите нашу ")
                        pushStringAnnotation("tag", "policy")
                        withStyle(SpanStyle(color = Color(0xFF00FFC8))) {
                            append("Политику конфиденциальности.")
                        }
                        pop()
                    }
                    ClickableText(
                        text = policyText,
                        style = TextStyle(
                            color = Color(0xFFB0BEC5),
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { offset ->
                            policyText.getStringAnnotations("tag", offset, offset).firstOrNull()?.let {
                                context.startActivity(
                                    Intent(Intent.ACTION_VIEW, Uri.parse("https://yourusername.github.io/pulsevpn-policy/privacy-policy"))
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Строка 2, 3, 4
                    Text(
                        text = "Нажмите Согласиться и продолжить, чтобы",
                        color = Color(0xFFB0BEC5),
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "принять",
                        color = Color(0xFFB0BEC5),
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    val termsText = buildAnnotatedString {
                        pushStringAnnotation("tag", "terms")
                        withStyle(SpanStyle(color = Color(0xFF00FFC8))) {
                            append("Условия использования.")
                        }
                        pop()
                    }
                    ClickableText(
                        text = termsText,
                        style = TextStyle(
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { offset ->
                            termsText.getStringAnnotations("tag", offset, offset).firstOrNull()?.let {
                                context.startActivity(
                                    Intent(Intent.ACTION_VIEW, Uri.parse("https://yourusername.github.io/pulsevpn-policy/terms-of-use"))
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 📄 Описание
                    Text(
                        text = "Принимая Условия использования и используя наш продукт, определённый набор данных устройства пользователей, включая информацию о приложениях, установленных пользователем, автоматически отправляется на серверы PulseVPN.",
                        color = Color(0xFFB0BEC5),
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Мы можем передавать некоторую контактную информацию, такую как список контактов, третьей стороне в маркетинговых и рекламных целях.",
                        color = Color(0xFFB0BEC5),
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Все собираемые нами данные анонимны и не содержат никакой личной идентифицирующей информации.",
                        color = Color(0xFFB0BEC5),
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }

                // 🟦 Кнопка
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF00FFC8), Color(0xFF0078A0))
                                )
                            )
                            .clickable { onAgree() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Согласиться & Продолжить",
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }

            // 💬 Диалог выхода
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = {
                        Text("Вы уверены, что хотите выйти?", color = Color.White)
                    },
                    text = {
                        Text(
                            "Согласно политике Google Play, мы не можем предоставить услуги, если вы не согласны. Если вы покинете страницу, приложение будет закрыто.",
                            color = Color(0xFFB0BEC5)
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            (context as? Activity)?.finish()
                        }) {
                            Text("Выйти", color = Color(0xFFFF5252))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Отмена", color = Color(0xFF00FFC8))
                        }
                    },
                    containerColor = Color(0xFF1A1F2E)
                )
            }
        }
    }
}
