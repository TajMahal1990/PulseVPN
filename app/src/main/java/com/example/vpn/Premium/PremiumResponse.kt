package com.example.vpn.Premium

data class PremiumResponse(
    val status: String,
    val expires_at: Long,
    val config: String? = null
)
