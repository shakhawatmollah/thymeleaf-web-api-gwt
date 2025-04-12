# Spring Boot Security Web App and APIs with GWT, Thymeleaf and PostgreSQL

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.1-orange)

A comprehensive Spring Boot application demonstrating custom Spring Security implementation with Thymeleaf for web interface and JWT authentication for API endpoints, using PostgreSQL as the database.

## Features

- **Custom Authentication**: Form-based login/logout with Spring Security
- **JWT Support**: Secure API endpoints with JWT authentication
- **Role-Based Access Control**: USER and ADMIN roles with different privileges
- **PostgreSQL Integration**: Persistent data storage for users and tokens
- **Thymeleaf Templates**: Server-side rendered views with Bootstrap 5
- **RESTful APIs**: Designed for both web and mobile clients
- **Refresh Tokens**: Secure token rotation mechanism

## Prerequisites

- Java 17 or higher
- PostgreSQL 15 or higher
- Maven 3.8+

## Installation

1. **Clone the repository**:
   ```bash
   git clone git@github.com:shakhawatmollah/thymeleaf-web-api-gwt.git
   cd thymeleaf-web-api-gwt
   ```

2. **Set up PostgreSQL**:
   - Create a new database named `spring_boot_auth`
   - Create a new schema named `auth_demo_app`
   - Update the database configuration in `application.properties`

3. **Configure the application**:
   Edit `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/spring_boot_auth?currentSchema=auth_demo_app
   spring.datasource.username=your_db_username
   spring.datasource.password=your_db_password
   spring.jpa.hibernate.ddl-auto=update
   ```

4. **Build and run the application**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

## Database Schema

The application will automatically create the following tables:

- `user`: Stores user credentials and roles
- `refresh_token`: Stores refresh tokens for JWT authentication

## Default Users

The application initializes with two test users:

| Email             | Password | Role  |
|-------------------|----------|-------|
| user@example.com  | password | USER  |
| admin@example.com | admin    | ADMIN |

## API Endpoints

### Authentication

| Endpoint          | Method | Description                     |
|-------------------|--------|---------------------------------|
| `/api/auth/login` | POST   | Authenticate and get JWT tokens |
| `/api/auth/refresh` | POST   | Refresh access token            |
| `/api/auth/logout` | POST   | Invalidate refresh token        |

### Protected Resources

| Endpoint       | Required Role | Description             |
|----------------|---------------|-------------------------|
| `/api/public`  | None          | Public test endpoint    |
| `/api/user`    | USER or ADMIN | User-level endpoint     |
| `/api/admin`   | ADMIN only    | Admin-level endpoint    |

## Web Interface

| Page      | URL          | Description                     |
|-----------|--------------|---------------------------------|
| Home      | `/`          | Welcome page with login link    |
| Login     | `/login`     | Custom login form               |
| Dashboard | `/home`      | Authenticated user dashboard    |

## Project Structure

```
src/
├── main/
│   ├── java/com/example/securitydemo/
│   │   ├── config/         # Security and application configuration
│   │   ├── controller/     # MVC and REST controllers
│   │   ├── dto/            # Data transfer objects
│   │   ├── entity/         # JPA entities
│   │   ├── exception/      # Custom exceptions
│   │   ├── repository/     # Data repositories
│   │   ├── service/        # Business logic
│   │   └── SecurityDemoApplication.java
│   └── resources/
│       ├── static/         # CSS, JS, images
│       ├── templates/      # Thymeleaf templates
│       └── application.properties
└── test/                   # Test classes
```

## Configuration

Key configuration options in `application.properties`:

```properties
# JWT Configuration
jwt.secret=your-256-bit-secret
jwt.expiration=86400000 # 24 hours
jwt.refresh-expiration=604800000 # 7 days

# Thymeleaf
spring.thymeleaf.cache=false # Disable in development
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/spring_boot_auth?currentSchema=auth_demo_app
spring.datasource.username=postgres
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

## Running Tests

To run the test suite:

```bash
mvn test
```

## Deployment

For production deployment:

1. Set `spring.thymeleaf.cache=true`
2. Configure a proper JWT secret key
3. Set up HTTPS for secure connections
4. Configure database connection pooling

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

## Acknowledgments

- Spring Security team
- PostgreSQL community
- Thymeleaf developers
- Bootstrap team for the frontend templates