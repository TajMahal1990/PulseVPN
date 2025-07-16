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


import android.app.Activity

import androidx.compose.ui.platform.LocalContext

import com.android.billingclient.api.*
import com.example.vpn.payment.BillingManager

@Composable
fun AccountScreen() {
    var selectedPlan by remember { mutableStateOf("month") }
    val context = LocalContext.current
    val billingManager = remember {
        BillingManager(
            context = context,
            onPremiumActive = { /* TODO: Handle premium state */ },
            onError = { it.printStackTrace() }
        )
    }
    val activity = context as? Activity
    var productDetails by remember { mutableStateOf<List<ProductDetails>>(emptyList()) }

    LaunchedEffect(Unit) {
        billingManager.startConnection()
        billingManager.queryProductDetails("monthly_sub") { monthly ->
            billingManager.queryProductDetails("yearly_sub") { yearly ->
                productDetails = monthly + yearly
            }
        }
    }

    val buttonText = if (selectedPlan == "year") "Try for 0.00" else "$7.99 / month"

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

        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("", modifier = Modifier.weight(1f))
            Text("Free", color = Color.Gray, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Text("Premium", color = Color(0xFF00FFC8), modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        }

        Spacer(Modifier.height(8.dp))

        ComparisonRow("🌍 Locations", "Germany only", "All countries")
        ComparisonRow("⚡ Speed", "Limited (5 Mbps)", "Unlimited")
        ComparisonRow("🔒 Encryption", "Standard", "Military-grade")
        ComparisonRow("🤖 Support", "Email only", "Personal assistant")
        ComparisonRow("⏱ Daily time limit", "15 min/day", "Unlimited")

        Spacer(Modifier.height(36.dp))
        Divider(color = Color.DarkGray.copy(alpha = 0.5f))
        Spacer(Modifier.height(36.dp))

        Text("Choose your plan", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
        Spacer(Modifier.height(16.dp))

        PlanOption("1-Year Plan", "$3.25 / month billed yearly", "3-day free trial, then \$39.00 / year", "Most popular", selectedPlan == "year") { selectedPlan = "year" }
        Spacer(Modifier.height(16.dp))
        PlanOption("1-Month Plan", "$7.99 / month", "", null, selectedPlan == "month") { selectedPlan = "month" }

        Spacer(Modifier.height(36.dp))
        Button(
            onClick = {
                val productId = if (selectedPlan == "month") "monthly_sub" else "yearly_sub"
                val product = productDetails.find { it.productId == productId }
                if (product != null && activity != null) {
                    billingManager.launchPurchaseFlow(activity, product)
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FFC8)),
            modifier = Modifier.fillMaxWidth().height(54.dp).clip(RoundedCornerShape(16.dp))
        ) {
            Text(buttonText, color = Color.Black, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(36.dp))
        Text("App version: 1.0.0", color = Color.Gray, fontSize = 12.sp)
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
fun PlanOption(title: String, subPrice: String, trialText: String, tag: String?, selected: Boolean, onClick: () -> Unit) {
    val border = if (selected) Modifier.border(2.dp, Color(0xFF00FFC8), RoundedCornerShape(18.dp)) else Modifier
    Column(
        modifier = Modifier.fillMaxWidth().then(border).clip(RoundedCornerShape(18.dp)).background(Color(0xFF101624)).clickable { onClick() }.padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            RadioButton(selected = selected, onClick = onClick, colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF00FFC8), unselectedColor = Color.White.copy(alpha = 0.5f)))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(subPrice, color = Color(0xFF00FFC8), fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                if (trialText.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(trialText, color = Color.Gray, fontSize = 13.sp)
                }
            }
            if (!tag.isNullOrBlank()) {
                Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(Color(0xFF00FFC8).copy(alpha = 0.15f)).padding(horizontal = 10.dp, vertical = 4.dp)) {
                    Text(tag, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF00FFC8))
                }
            }
        }
    }
}

@Composable
fun ComparisonRow(feature: String, basicValue: String, premiumValue: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(feature, color = Color.White, fontSize = 15.sp, modifier = Modifier.weight(1.5f))
        Text(basicValue, color = Color.Gray, fontSize = 14.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
        Text(premiumValue, color = Color(0xFF00FFC8), fontSize = 14.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0F1C)
@Composable
fun AccountScreenPreview() {
    AccountScreen()
}
