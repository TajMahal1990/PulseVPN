package com.example.vpn.Premium

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("auth")
    suspend fun authenticate(
        @Body request: AuthRequest,
        @Header("X-API-Key") apiKey: String = "supersecret123" // по умолчанию, можно переопределить
    ): PremiumResponse
}
