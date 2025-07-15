package com.example.vpn.Premium



import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.time.LocalDate

class FreePlanLimiter(
    context: Context,
    private val dailyLimitSec: Int = 15 * 60
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("free_plan_prefs", Context.MODE_PRIVATE)

    private var today: LocalDate = LocalDate.now()
    private var remainingSecState by mutableStateOf(loadRemaining())

    private fun loadRemaining(): Int {
        val savedDate = prefs.getString("last_date", null)
        val savedSec = prefs.getInt("remaining_sec", dailyLimitSec)
        return if (savedDate == today.toString()) savedSec else dailyLimitSec
    }

    private fun save() {
        prefs.edit()
            .putString("last_date", today.toString())
            .putInt("remaining_sec", remainingSecState)
            .apply()
    }

    fun tick(): Boolean {
        resetIfNewDay()
        if (remainingSecState > 0) {
            remainingSecState--
            save()
        }
        return remainingSecState > 0
    }

    fun resetIfNewDay() {
        val storedDate = prefs.getString("last_date", null)
        if (storedDate != today.toString()) {
            today = LocalDate.now()
            remainingSecState = dailyLimitSec
            save()
        }
    }

    fun remaining(): Int = remainingSecState
}
