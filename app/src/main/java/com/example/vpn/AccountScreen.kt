package com.example.vpn

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AccountScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0F1C))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Premium Features", style = MaterialTheme.typography.titleLarge.copy(fontSize = 28.sp), color = Color.White)
        Spacer(modifier = Modifier.height(24.dp))

        // Comparison Section
        Text("What’s included", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.width(100.dp))
            Text("Basic", color = Color.Gray, fontSize = 14.sp)
            Text("Premium", color = Color(0xFF00FFC8), fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        ComparisonRow("\uD83C\uDF0D Germany / Netherlands", true, true)
        ComparisonRow("\uD83C\uDF0D All countries", false, true)
        ComparisonRow("⚡ Faster connection", false, true)
        ComparisonRow("\uD83D\uDD1A Priority support", false, true)
        ComparisonRow("\uD83D\uDEAB No ads", false, true)

        Spacer(modifier = Modifier.height(36.dp))
        Divider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 1.dp)
        Spacer(modifier = Modifier.height(36.dp))

        // Plan Grid
        Text("Choose your plan", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Column {
            PlanOption("1 Month", "$3.99 / month", "3 дня бесплатно, далее $3.99 в месяц", null, selected = true)
            Spacer(modifier = Modifier.height(12.dp))
            PlanOption("12 Months", "$29.99 / year", "3 дня бесплатно, далее $29.99 в год", "Save 38% • You save $2.33/mo", selected = false)
        }

        Spacer(modifier = Modifier.height(36.dp))

        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FFC8)),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Text("Go Premium \uD83D\uDE80", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 17.sp)
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text("App version: 1.0.0", fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun PlanOption(duration: String, price: String, tag: String?, savings: String?, selected: Boolean) {
    val border = if (selected) Modifier.border(2.dp, Color(0xFF00FFC8), RoundedCornerShape(18.dp)) else Modifier

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(border)
            .clip(RoundedCornerShape(18.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF00FFC8), Color(0xFF0078A0))
                )
            )
            .clickable { }
            .padding(vertical = 20.dp, horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(duration, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(price, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                if (savings != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(savings, color = Color.White, fontSize = 12.sp)
                }
            }
            if (tag != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.3f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(tag, fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ComparisonRow(feature: String, basic: Boolean, premium: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(feature, color = Color.White, fontSize = 15.sp)
        Row {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = if (basic) Color(0xFF00FFC8) else Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(24.dp))
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = if (premium) Color(0xFF00FFC8) else Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
