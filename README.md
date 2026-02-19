#  SkillLink - Location-Based Technician Marketplace API


SkillLink is a production-ready Spring Boot backend API designed to
power a location-aware service marketplace where users can automatically
discover nearby technicians based on real-time geographic data.

This system integrates secure authentication (JWT & Google OAuth2),
Redis caching, SMTP email services, Swagger documentation, and automatic
location tracking for accurate technician discovery.

It is built following enterprise backend architecture principles and is
fully containerized using Docker.

------------------------------------------------------------------------

# Project Vision

SkillLink solves a practical real-world problem:

1. Connecting users with nearby technicians based on specialization and
real-time geographic location.

2. Users can: Automatically submit their current location, Search by
technician specialization, Receive nearby, filtered results instantly

The system is designed to be secure, scalable, and production-ready.

------------------------------------------------------------------------

#  Core Features
##  Authentication & Security

-   JWT-based authentication
-   Google OAuth2 registration & login
-   Secure technician login
-   Stateless session management
-   Secure password handling
-   Environment-based secret configuration

------------------------------------------------------------------------

##  Automatic Location-Based Search

-   User latitude & longitude collected automatically\
-   Technician latitude & longitude stored in the system\
-   Backend filters technicians by specialization and geographic
    proximity

Example endpoint:

GET /api/users/search?specialization=plumber

The backend: - Extracts user location\
- Filters technicians by specialization\
- Compares coordinates\
- Returns nearby results

------------------------------------------------------------------------

## Redis Caching

-   Frequently requested search results are cached
-   Reduces database load
-   Improves response time
-   Enhances scalability

------------------------------------------------------------------------

## Email Integration (SMTP)

-   Email notifications supported
-   Secure SMTP configuration
-   Ready for verification or transactional messaging

------------------------------------------------------------------------

##  API Documentation -- Swagger UI

Access Swagger UI at:

http://localhost:8080/swagger-ui.html

Provides: - Live API testing
- Endpoint documentation
- Request/response models
- Authentication testing support

------------------------------------------------------------------------

##  Input Validation

-   Bean validation annotations\
-   Structured error responses\
-   Clean request validation\
-   Strong API boundary enforcement

------------------------------------------------------------------------

# 🛠 Technology Stack

-   Java
-   Spring Boot
-   Spring Security
-   JWT
-   Google OAuth2
-   MySQL
-   Redis
-   SMTP
-   Swagger UI
-   Spring Data JPA
-   Maven
-   Docker
-   ImgBB

------------------------------------------------------------------------

#  Architecture

Layered Architecture:

Controller → Config → DTO → Service → Repository → Database 

### Controller Layer

Handles HTTP requests and validation.

### Service Layer

Contains business logic, authentication flow, geo-filtering, and caching
logic.

### Repository Layer

Handles data access via JPA and MySQL.

### Security Layer

Implements JWT filters, OAuth2 configuration, and stateless
authentication.

------------------------------------------------------------------------

#  Dockerized Deployment

## Build Application

mvn clean package

## Run with Docker

docker compose up --build

## Stop Containers

docker compose down

## Reset Database

docker compose down -v

------------------------------------------------------------------------

#  Environment Configuration

Externalized configuration includes:

-   Database credentials\
-   JWT secret\
-   Google OAuth credentials\
-   SMTP credentials\
-   Redis configuration

No sensitive data is hardcoded.

------------------------------------------------------------------------

# 🔐 Security Design

-   Stateless authentication\
-   Token-based authorization\
-   OAuth2 social login\
-   Environment-based secret management\
-   Input validation enforcement

------------------------------------------------------------------------

# 📡 API Base URL

http://localhost:8080

Swagger UI:

http://localhost:8080/swagger-ui.html

------------------------------------------------------------------------

# 📈 Scalability & Production Readiness

-   Stateless architecture
-   Redis caching layer
-   Containerized infrastructure
-   Clean layered design
-   Environment-driven configuration
-   CI/CD automation


Ready for deployment on VPS or cloud platforms.

------------------------------------------------------------------------

# 👨‍💻 Author

AJIBO CHIBUZOR ANTHONY\
Backend Engineer \| Java & Spring Boot

Focused on building scalable, secure, and real-world backend systems.

------------------------------------------------------------------------

#  Future Enhancements

-   Advanced distance calculation optimization
-   Role-based access control (RBAC)
-   Rate limiting
-   Monitoring & logging
-   Cloud-native deployment pipeline