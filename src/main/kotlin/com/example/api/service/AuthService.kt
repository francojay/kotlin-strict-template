package com.example.api.service

import com.example.api.model.User
import com.example.api.repository.UserRepository
import com.example.api.security.JwtUtil
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {
    private val logger = LoggerFactory.getLogger(AuthService::class.java)

    fun authenticate(username: String, password: String): String {
        logger.info("Attempting authentication for user: $username")
        
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(username, password)
            )
            logger.info("Authentication successful for user: $username")
            return jwtUtil.generateToken(username)
        } catch (e: Exception) {
            logger.error("Authentication failed for user: $username")
            throw IllegalArgumentException("Authentication failed")
        }
    }

    fun register(username: String, password: String): String {
        logger.info("Attempting registration for user: $username")
        if (userRepository.findByUsername(username) != null) {
            logger.error("Username already exists: $username")
            throw IllegalStateException("Username already exists")
        }

        val hashedPassword = passwordEncoder.encode(password)
        logger.debug("Password hashed for user: $username")

        val user = User(
            username = username,
            password = hashedPassword
        )

        userRepository.save(user)
        logger.info("User registered successfully: $username")
        return jwtUtil.generateToken(username)
    }
} 