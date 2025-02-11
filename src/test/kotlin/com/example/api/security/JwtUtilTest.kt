package com.example.api.security

import io.jsonwebtoken.SignatureException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.test.util.ReflectionTestUtils
import java.util.Date
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull

class JwtUtilTest {

    private lateinit var jwtUtil: JwtUtil
    private val secret = "test-secret-key-for-jwt-token-generation-in-tests-must-be-at-least-256-bits"

    @BeforeEach
    fun setup() {
        jwtUtil = JwtUtil()
        ReflectionTestUtils.setField(jwtUtil, "secret", secret)
    }

    @Test
    fun `generateToken should create valid JWT token`() {
        // Arrange
        val username = "testuser"

        // Act
        val token = jwtUtil.generateToken(username)

        // Assert
        assertNotNull(token)
        assertTrue(token.isNotEmpty())
        assertEquals(username, jwtUtil.extractUsername(token))
    }

    @Test
    fun `validateToken should return true for valid token`() {
        // Arrange
        val username = "testuser"
        val token = jwtUtil.generateToken(username)

        // Act & Assert
        assertTrue(jwtUtil.validateToken(token))
    }

    @Test
    fun `validateToken should return false for tampered token`() {
        // Arrange
        val username = "testuser"
        val token = jwtUtil.generateToken(username) + "tampered"

        // Act & Assert
        assertFalse(jwtUtil.validateToken(token))
    }

    @Test
    fun `validateToken should return false for invalid token format`() {
        // Arrange
        val invalidToken = "invalid.token.here"

        // Act & Assert
        assertFalse(jwtUtil.validateToken(invalidToken))
    }

    @Test
    fun `extractUsername should return correct username from token`() {
        // Arrange
        val username = "testuser"
        val token = jwtUtil.generateToken(username)

        // Act
        val extractedUsername = jwtUtil.extractUsername(token)

        // Assert
        assertEquals(username, extractedUsername)
    }

    @Test
    fun `extractUsername should return null for invalid token`() {
        // Arrange
        val invalidToken = "invalid.token.here"

        // Act
        val extractedUsername = jwtUtil.extractUsername(invalidToken)

        // Assert
        assertNull(extractedUsername)
    }
} 