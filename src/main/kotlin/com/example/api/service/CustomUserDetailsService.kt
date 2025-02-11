package com.example.api.service

import com.example.api.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    private val logger = LoggerFactory.getLogger(CustomUserDetailsService::class.java)
    
    override fun loadUserByUsername(username: String): UserDetails { 
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found: $username")

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.username)
            .password(user.password)
            .roles("user")
            .build()
    }
} 