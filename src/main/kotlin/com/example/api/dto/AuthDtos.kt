package com.example.api.dto

data class AuthRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val password: String
)

data class AuthResponse(
    val token: String? = null,
    val success: Boolean = false,
    val error: String? = null
) 