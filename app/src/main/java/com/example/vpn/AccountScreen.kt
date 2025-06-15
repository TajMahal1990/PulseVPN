package com.example.vpn

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AccountScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Your Account", style = MaterialTheme.typography.titleLarge.copy(fontSize = 26.sp), color = Color.White)
        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF11182A))
                .padding(20.dp)
        ) {
            Column {
                Text("Current Plan: Basic", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "You’re connected to a Basic Plan. Upgrade for faster speeds and full server access.",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text("Choose your plan", color = Color.White, style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp))
        Spacer(modifier = Modifier.height(16.dp))

        PlanOption("1 Month", "$3.99", null, null)
        Spacer(modifier = Modifier.height(12.dp))
        PlanOption("3 Months", "$9.99", "Save 17%", "You save $1.33/month")
        Spacer(modifier = Modifier.height(12.dp))
        PlanOption("12 Months", "$29.99", "Save 38%", "You save $2.33/month")

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = { /* Handle upgrade */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FFC8)),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Text("Upgrade Plan", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text("Compare Plans", color = Color.White, style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp))
        Spacer(modifier = Modifier.height(16.dp))

        ComparisonRow("Germany / Netherlands", true, true)
        ComparisonRow("All countries", false, true)
        ComparisonRow("Faster connection", false, true)
        ComparisonRow("Priority support", false, true)
        ComparisonRow("No ads", false, true)

        Spacer(modifier = Modifier.height(36.dp))

        Text("App version: 1.0.0", fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun PlanOption(duration: String, price: String, tag: String?, savings: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF00FFC8), Color(0xFF0078A0))
                )
            )
            .clickable { /* Handle selection */ }
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
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(tag, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Medium)
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