package com.example.api.service

import com.example.api.model.User
import com.example.api.repository.UserRepository
import com.example.api.security.JwtUtil
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.assertEquals

class AuthServiceTest {
    private lateinit var authService: AuthService
    private lateinit var authenticationManager: AuthenticationManager
    private lateinit var userRepository: UserRepository
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var jwtUtil: JwtUtil

    @BeforeEach
    fun setUp() {
        authenticationManager = mock(AuthenticationManager::class.java)
        userRepository = mock(UserRepository::class.java)
        passwordEncoder = mock(PasswordEncoder::class.java)
        jwtUtil = mock(JwtUtil::class.java)
        authService = AuthService(authenticationManager, userRepository, passwordEncoder, jwtUtil)
    }

    @Test
    fun `authenticate should return token when credentials are valid`() {
        // Arrange
        val username = "testuser"
        val password = "password"
        val token = "jwt.token.here"
        val authentication = UsernamePasswordAuthenticationToken(username, password)
        
        `when`(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken::class.java)))
            .thenReturn(authentication)
        `when`(jwtUtil.generateToken(username)).thenReturn(token)

        // Act
        val result = authService.authenticate(username, password)

        // Assert
        assertEquals(token, result)
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken::class.java))
        verify(jwtUtil).generateToken(username)
    }

    @Test
    fun `authenticate should throw exception when credentials are invalid`() {
        // Arrange
        val username = "testuser"
        val password = "wrongpassword"
        
        `when`(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken::class.java)))
            .thenThrow(BadCredentialsException("Invalid credentials"))

        // Act & Assert
        assertThrows<IllegalArgumentException> {
            authService.authenticate(username, password)
        }
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken::class.java))
        verify(jwtUtil, never()).generateToken(anyString())
    }

    @Test
    fun `register should create new user and return token when username is available`() {
        // Arrange
        val username = "newuser"
        val password = "password"
        val hashedPassword = "hashedPassword"
        val token = "jwt.token.here"
        val user = User(username = username, password = hashedPassword)
        
        `when`(userRepository.findByUsername(username)).thenReturn(null)
        `when`(passwordEncoder.encode(password)).thenReturn(hashedPassword)
        `when`(userRepository.save(any(User::class.java))).thenReturn(user)
        `when`(jwtUtil.generateToken(username)).thenReturn(token)

        // Act
        val result = authService.register(username, password)

        // Assert
        assertEquals(token, result)
        verify(userRepository).findByUsername(username)
        verify(passwordEncoder).encode(password)
        verify(userRepository).save(any(User::class.java))
        verify(jwtUtil).generateToken(username)
    }

    @Test
    fun `register should throw exception when username already exists`() {
        // Arrange
        val username = "existinguser"
        val password = "password"
        val existingUser = User(username = username, password = "somepassword")
        
        `when`(userRepository.findByUsername(username)).thenReturn(existingUser)

        // Act & Assert
        assertThrows<IllegalStateException> {
            authService.register(username, password)
        }
        verify(userRepository).findByUsername(username)
        verify(passwordEncoder, never()).encode(anyString())
        verify(userRepository, never()).save(any(User::class.java))
        verify(jwtUtil, never()).generateToken(anyString())
    }
} 