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
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.billingclient.api.ProductDetails
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.text.style.TextAlign


@Composable
fun AccountScreen() {
    var selectedPlan by remember { mutableStateOf("month") }
    val selectedPrice = if (selectedPlan == "year") "$39.00 / year" else "$7.99 / month"


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0F1C))
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Premium Features", style = MaterialTheme.typography.titleLarge.copy(fontSize = 28.sp), color = Color.White)
        Spacer(Modifier.height(24.dp))

        Text("What’s included", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "",
                modifier = Modifier.weight(1f) // пустая ячейка под описание фичи
            )
            Text(
                "Free",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.weight(0.5f),
                textAlign = TextAlign.Center
            )
            Text(
                "Premium",
                color = Color(0xFF00FFC8),
                fontSize = 14.sp,
                modifier = Modifier.weight(0.5f),
                textAlign = TextAlign.Center
            )
        }


        Spacer(Modifier.height(8.dp))

        ComparisonRow("🌍 Locations", "Germany only", "All countries")
        ComparisonRow("⚡ Speed", "Limited (5 Mbps)", "Unlimited")
        ComparisonRow("🔒 Encryption", "Standard", "Military-grade")
        ComparisonRow("📞 Support", "Email only", "Personal assistant")
        ComparisonRow("⏱ Daily time limit", "15 min/day", "Unlimited")




        Spacer(Modifier.height(36.dp))
        Divider(color = Color.DarkGray.copy(alpha = 0.5f), thickness = 1.dp)
        Spacer(Modifier.height(36.dp))

        Text("Choose your plan", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
        Spacer(Modifier.height(16.dp))

        PlanOption(
            title = "1-Year Plan",
            subPrice = "$3.25/mo billed yearly",
            trialText = "3-day free trial, then $39.00/year",
            tag = "Most popular",
            selected = selectedPlan == "year",
            onClick = { selectedPlan = "year" }
        )

        Spacer(modifier = Modifier.height(16.dp))

        PlanOption(
            title = "1-Month Plan",
            subPrice = "$7.99/mo",
            trialText = "3-day free trial, then $7.99/month",
            tag = null,
            selected = selectedPlan == "month",
            onClick = { selectedPlan = "month" }
        )

        Spacer(Modifier.height(36.dp))

        Button(
            onClick = { /* TODO: handle purchase */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FFC8)),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Text(
                text = selectedPrice,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        }

        Spacer(Modifier.height(36.dp))
        Text("App version: 1.0.0", fontSize = 12.sp, color = Color.Gray)
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
fun PlanOption(
    title: String,              // "1-Year Plan" или "1-Month Plan"
    subPrice: String,           // "$3.25/mo billed yearly" или "$7.99/mo"
    trialText: String,          // "3-day free trial, then $39.00/year"
    tag: String?,               // "Most popular" или null
    selected: Boolean,
    onClick: () -> Unit
) {
    val border = if (selected)
        Modifier.border(2.dp, Color(0xFF00FFC8), RoundedCornerShape(18.dp))
    else Modifier

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(border)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF101624)) // тёмный фон
            .clickable { onClick() }
            .padding(vertical = 20.dp, horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF00FFC8),
                    unselectedColor = Color.White.copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subPrice,
                    color = Color(0xFF00FFC8),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = trialText,
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }

            if (!tag.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF00FFC8).copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = tag,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF00FFC8)
                    )
                }
            }
        }
    }
}

@Composable
fun ComparisonRow(
    feature: String,
    basicValue: String,     // пример: "15 min/day"
    premiumValue: String    // пример: "Unlimited"
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Название фичи
        Text(
            text = feature,
            color = Color.White,
            fontSize = 15.sp,
            modifier = Modifier.weight(1.5f)
        )

        // Значение для Basic
        Text(
            text = basicValue,
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )

        // Значение для Premium
        Text(
            text = premiumValue,
            color = Color(0xFF00FFC8),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0F1C)
@Composable
fun AccountScreenPreview() {
    AccountScreen()
}
