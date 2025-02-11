# Kotlin Spring Boot Template

A secure REST API template built with Kotlin and Spring Boot, featuring JWT authentication and MySQL integration.

## Features
- JWT-based authentication
- User registration and login
- MySQL database with Flyway migrations
- Strict code style enforcement
- Secure password hashing
- Comprehensive test coverage

## Requirements
- JDK 17
- Docker & Docker Compose
- Gradle 8.x

## Setup
1. Start MySQL using Docker Compose:
```bash
docker-compose up -d
```

2. Run the application:
```bash
./gradlew bootRun
```

## API Endpoints
- `POST /auth/register` - Register new user
- `POST /auth/login` - Authenticate user and get JWT token

## Development
- Tests: `./gradlew test`
- Build: `./gradlew build`
- Run: `./run.sh` (convenience script that kills any process on port 8080 before starting the app) 