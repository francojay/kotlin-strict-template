package com.example.api

import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.assertj.core.api.Assertions.assertThat

class PasswordHashTest {
    @Test
    fun `test password hash`() {
        val encoder = BCryptPasswordEncoder()
        val password = "password"
        val hash = encoder.encode(password)
        println("Generated hash for password '$password': $hash")
        assertThat(encoder.matches(password, hash)).isTrue()
    }
} 