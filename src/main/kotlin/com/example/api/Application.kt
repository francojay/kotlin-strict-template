package com.example.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EntityScan("com.example.api.model")
@EnableJpaRepositories("com.example.api.repository")
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}