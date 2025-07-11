package com.example.vpn.mainActivity


data class VpnServer(
    val country: String,        // Название страны (например, "Germany")
    val city: String,           // Город (например, "Frankfurt")
    val code: String,           // Код страны для флага (например, "DE")
    val signalLevel: Int,       // Уровень сигнала (1–3)
    val isAvailable: Boolean,   // Доступен ли сервер (для блокировки)
    val config: String          // WireGuard-конфиг
)
