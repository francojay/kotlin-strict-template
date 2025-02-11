package com.example.api.repository

import com.example.api.model.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setup() {
        userRepository.deleteAll()
    }

    @Test
    fun `findByUsername should return user when username exists`() {
        // Arrange
        val username = "testuser"
        val password = "password"
        val user = User(username = username, password = password)
        entityManager.persist(user)
        entityManager.flush()

        // Act
        val found = userRepository.findByUsername(username)

        // Assert
        assertNotNull(found)
        assertEquals(username, found?.username)
        assertEquals(password, found?.password)
    }

    @Test
    fun `findByUsername should return null when username does not exist`() {
        // Act
        val found = userRepository.findByUsername("nonexistent")

        // Assert
        assertNull(found)
    }

    @Test
    fun `existsByUsername should return true when username exists`() {
        // Arrange
        val username = "testuser"
        val user = User(username = username, password = "password")
        entityManager.persist(user)
        entityManager.flush()

        // Act
        val exists = userRepository.existsByUsername(username)

        // Assert
        assertTrue(exists)
    }

    @Test
    fun `existsByUsername should return false when username does not exist`() {
        // Act
        val exists = userRepository.existsByUsername("nonexistent")

        // Assert
        assertFalse(exists)
    }

    @Test
    fun `save should persist new user`() {
        // Arrange
        val username = "newuser"
        val password = "password"
        val user = User(username = username, password = password)

        // Act
        val saved = userRepository.save(user)

        // Assert
        assertNotNull(saved.id)
        assertEquals(username, saved.username)
        assertEquals(password, saved.password)

        // Verify it's in the database
        val found = entityManager.find(User::class.java, saved.id)
        assertNotNull(found)
        assertEquals(username, found.username)
        assertEquals(password, found.password)
    }
} 