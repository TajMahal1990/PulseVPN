package com.example.vpn
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FilterScreen(
    onAppSelectionChanged: (packageName: String, isSelected: Boolean) -> Unit
) {
    val context = LocalContext.current
    val pm = context.packageManager

    // Все пользовательские приложения с иконкой
    val allApps = remember {
        pm.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { app ->
                isUserApp(app) && app.loadIconSafe(pm) != null
            }
            .sortedBy { it.loadLabelSafe(pm) }
    }


    val selectedApps = remember { mutableStateListOf<String>() }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "Choose Apps for VPN",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White.copy(alpha = 0.95f),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        allApps.forEach { app ->
            val isSelected = selectedApps.contains(app.packageName)
            val icon = app.loadIconSafe(pm)
            val label = app.loadLabelSafe(pm)

            FilterAppCard(
                icon = icon,
                name = label,
                isSelected = isSelected,
                onToggle = { checked ->
                    if (checked) {
                        selectedApps.add(app.packageName)
                    } else {
                        selectedApps.remove(app.packageName)
                    }
                    onAppSelectionChanged(app.packageName, checked)
                }
            )
        }
    }
}



@Composable
fun FilterAppCard(
    icon: Drawable?,
    name: String,
    isSelected: Boolean,
    onToggle: (Boolean) -> Unit
) {
    val iconBitmap = remember(icon) {
        (icon as? BitmapDrawable)?.bitmap?.asImageBitmap()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1C2232).copy(alpha = 0.85f) // тёмно-фиолетово-синий с прозрачностью
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            iconBitmap?.let {
                Image(
                    bitmap = it,
                    contentDescription = name,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
            } ?: Spacer(modifier = Modifier.size(48.dp))

            Spacer(modifier = Modifier.width(18.dp))

            Text(
                text = name,
                color = Color.White,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Switch(
                checked = isSelected,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF00FFC6),
                    checkedTrackColor = Color(0xFF00FFC6).copy(alpha = 0.4f),
                    uncheckedThumbColor = Color(0xFF888888),
                    uncheckedTrackColor = Color(0xFF444444).copy(alpha = 0.3f)
                )
            )
        }
    }
}


fun ApplicationInfo.loadLabelSafe(pm: PackageManager): String {
    return try {
        loadLabel(pm)?.toString() ?: packageName
    } catch (e: Exception) {
        packageName
    }
}

fun ApplicationInfo.loadIconSafe(pm: PackageManager): Drawable? {
    return try {
        loadIcon(pm)
    } catch (e: Exception) {
        null
    }
}
fun isUserApp(app: ApplicationInfo): Boolean {
    return (app.flags and ApplicationInfo.FLAG_SYSTEM == 0) ||
            (app.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0)
}
