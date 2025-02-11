package com.example.api.controller

import com.example.api.dto.AuthRequest
import com.example.api.dto.AuthResponse
import com.example.api.dto.RegisterRequest
import com.example.api.service.AuthService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val authService: AuthService) {
    private val logger = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequest): ResponseEntity<AuthResponse> {
        return try {
            val token = authService.authenticate(request.username, request.password)
            ResponseEntity.ok(AuthResponse(token = token, success = true))
        } catch (e: Exception) {
            logger.error("Login failed for user: ${request.username}", e)
            ResponseEntity.status(401).body(AuthResponse(error = "Invalid username or password"))
        }
    }

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        return try {
            val token = authService.register(request.username, request.password)
            ResponseEntity.ok(AuthResponse(token = token, success = true))
        } catch (e: IllegalStateException) {
            logger.error("Registration failed for user: ${request.username}", e)
            ResponseEntity.badRequest().body(AuthResponse(error = "Username already exists"))
        } catch (e: Exception) {
            logger.error("Registration failed for user: ${request.username}", e)
            ResponseEntity.badRequest().body(AuthResponse(error = "Registration failed"))
        }
    }
} 