package com.example.vpn.mainActivity



data class VpnLocation(
    val country: String,
    val city: String,
    val code: String,
    val signalLevel: Int,
    val isAvailable: Boolean
)



fun getDefaultLocations(): List<VpnLocation> = listOf(
    VpnLocation("Germany", "Frankfurt", "DE", 3, true),
    VpnLocation("Netherlands", "Amsterdam", "NL", 3, true),
    VpnLocation("USA", "New York", "US", 2, false),
    VpnLocation("France", "Paris", "FR", 1, false),
    VpnLocation("UK", "London", "GB", 2, false)
)