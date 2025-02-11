package com.example.api.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

@Component
class RequestIdFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        try {
            val requestId = UUID.randomUUID().toString()
            MDC.put("requestId", requestId)
            response.addHeader("X-Request-ID", requestId)
            chain.doFilter(request, response)
        } finally {
            MDC.clear()
        }
    }
} 