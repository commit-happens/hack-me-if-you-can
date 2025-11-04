# Hack Me If You Can - Backend 🔐

A Spring Boot REST API backend for the "Hack Me If You Can" phishing awareness game. This backend manages player scores and provides endpoints for the frontend application.

## 📋 Table of Contents

- [Java & Spring Boot Basics](#java--spring-boot-basics)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Database Configuration](#database-configuration)
- [Testing](#testing)
- [Features](#features)

## ☕ Java & Spring Boot Basics

### What is Java?
Java is a popular, object-oriented programming language that runs on the Java Virtual Machine (JVM). It's known for its "write once, run anywhere" philosophy, meaning Java code can run on any platform that has a JVM installed.

**Key Java Concepts:**
- **Classes and Objects**: Java is object-oriented, meaning everything is organized into classes (blueprints) and objects (instances)
- **Packages**: Organize related classes together (like folders for your code)
- **Annotations**: Special markers that provide metadata about your code (start with @)
- **Inheritance**: Classes can inherit properties and methods from other classes
- **Interfaces**: Contracts that define what methods a class must implement

### What is Spring Boot?
Spring Boot is a framework that makes it easy to create production-ready Java applications. It provides:
- **Auto-configuration**: Automatically sets up common configurations
- **Embedded server**: Runs your app without needing a separate server
- **Dependency injection**: Automatically provides objects where needed
- **REST APIs**: Easy creation of web APIs
- **Database integration**: Simple database connectivity

**Spring Boot Key Annotations:**
- `@SpringBootApplication`: Marks the main class of your Spring Boot app
- `@RestController`: Creates a class that handles web requests and returns JSON/XML
- `@RequestMapping`: Maps web requests to specific methods
- `@Service`: Marks a class as a business logic service
- `@Repository`: Marks a class as a data access layer
- `@Entity`: Marks a class as a database table
- `@Autowired`: Automatically injects dependencies

## 🏗️ Project Structure

This project follows the standard Maven directory structure and Spring Boot best practices:

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/hackme/backend/          # Main source code
│   │   │   ├── HackMeBackendApplication.java # Main application entry point
│   │   │   ├── config/                       # Configuration classes
│   │   │   │   └── WebConfig.java           # Web/CORS configuration
│   │   │   ├── controller/                   # REST API endpoints (web layer)
│   │   │   │   ├── GlobalExceptionHandler.java # Handles errors globally
│   │   │   │   └── PlayerController.java    # Player-related API endpoints
│   │   │   ├── dto/                          # Data Transfer Objects (API contracts)
│   │   │   │   ├── PlayerScoreRequest.java  # Request format for scores
│   │   │   │   └── PlayerScoreResponse.java # Response format for scores
│   │   │   ├── entity/                       # Database models
│   │   │   │   └── Player.java              # Player database table
│   │   │   ├── repository/                   # Data access layer
│   │   │   │   └── PlayerRepository.java    # Database operations for Player
│   │   │   └── service/                      # Business logic layer
│   │   │       ├── PlayerNotFoundException.java # Custom exception
│   │   │       └── PlayerService.java       # Player business logic
│   │   └── resources/
│   │       └── application.properties        # Application configuration
│   └── test/
│       └── java/                            # Test source code
├── pom.xml                                  # Maven build configuration
└── README.md                                # This file
```

### Layer Architecture Explained

**Spring Boot follows a layered architecture:**

1. **Controller Layer** (`controller/`): 
   - Handles HTTP requests and responses
   - Maps URLs to specific methods
   - Validates input and formats output
   - Think of it as the "front desk" of your application

2. **Service Layer** (`service/`):
   - Contains business logic
   - Processes data and applies rules
   - Acts as a bridge between controllers and repositories
   - Think of it as the "brain" of your application

3. **Repository Layer** (`repository/`):
   - Handles database operations (CRUD: Create, Read, Update, Delete)
   - Abstracts database complexity
   - Think of it as the "librarian" managing your data

4. **Entity Layer** (`entity/`):
   - Represents database tables as Java classes
   - Defines the structure of your data
   - Think of it as the "blueprint" for your data

5. **DTO Layer** (`dto/`):
   - Data Transfer Objects for API communication
   - Defines what data goes in/out of your API
   - Think of it as the "contract" between frontend and backend

## 🚀 Getting Started

### Prerequisites

- **Java 21** or higher (recently upgraded from Java 17!)
- **Maven 3.6** or higher
- **MySQL 8.0** or higher (or use H2 for development)

### Installation and Running

1. **Clone the repository** (if you haven't already):
   ```bash
   git clone <repository-url>
   cd hack-me-if-you-can/backend
   ```

2. **Build the project**:
   ```bash
   mvn clean compile
   ```

3. **Run tests**:
   ```bash
   mvn test
   ```

4. **Start the application**:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## 🔧 Features

- **REST API for player management**: Create, read, update player scores
- **MySQL database integration**: Persistent data storage
- **Player score tracking**: Track and manage game scores
- **CORS configuration**: Allows frontend integration
- **Input validation and error handling**: Robust error management
- **Java 21 support**: Latest LTS Java version for better performance

## 📡 API Endpoints

### Player Management

#### Get All Players
```http
GET /api/players
```
**Response:**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "score": 85,
    "createdAt": "2025-11-03T14:00:00Z"
  }
]
```

#### Get Player by ID
```http
GET /api/players/{id}
```
**Response:**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com", 
  "score": 85,
  "createdAt": "2025-11-03T14:00:00Z"
}
```

#### Create New Player
```http
POST /api/players
Content-Type: application/json

{
  "name": "Jane Smith",
  "email": "jane@example.com"
}
```

#### Update Player Score
```http
PUT /api/players/{id}/score
Content-Type: application/json

{
  "score": 95
}
```

## 🗄️ Database Configuration

### Development Setup (H2 In-Memory Database)
For development, the application uses H2 in-memory database by default:

```properties
# application.properties (current setup)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.h2.console.enabled=true
spring.jpa.show-sql=true
```

Access H2 Console: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (leave empty)

#### Production Setup (MySQL)

1. Create a MySQL database:
```sql
CREATE DATABASE hackme_db;
```

2. Update `application.properties` for production:
```properties
# MySQL Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/hackme_db
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.show-sql=false
```

## 🧪 Testing

### Running Tests
```bash
# Run all tests
mvn test

# Run tests with coverage
mvn clean test jacoco:report
```

### Test Structure
- **Unit Tests**: Test individual components in isolation
- **Integration Tests**: Test how components work together
- **Repository Tests**: Test database operations
- **Controller Tests**: Test REST endpoints

## 🚀 Development Workflow

### 1. **Setup Development Environment**
```bash
# Navigate to backend directory
cd backend

# Install dependencies
mvn clean install
```

### 2. **Run in Development Mode**
```bash
# Start with automatic restart on changes
mvn spring-boot:run
```

### 3. **Access Development Tools**
- **Application**: `http://localhost:8080`
- **H2 Database Console**: `http://localhost:8080/h2-console`
- **Actuator Health Check**: `http://localhost:8080/actuator/health`

### 4. **Build for Production**
```bash
# Create executable JAR
mvn clean package

# Run the JAR file
java -jar target/hack-me-backend-1.0.0.jar
```

## 🛠️ Common Development Tasks

### Adding New API Endpoint
1. **Create DTO classes** in `dto/` package
2. **Add method to Controller** with proper mapping
3. **Implement business logic** in Service layer
4. **Add repository method** if database access needed
5. **Write tests** for the new functionality

### Adding New Database Entity
1. **Create Entity class** in `entity/` package
2. **Create Repository interface** in `repository/` package
3. **Update Service layer** to use new entity
4. **Add migration script** or let Hibernate auto-create

## 📚 Learning Resources

### Java & Spring Boot
- [Spring Boot Official Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring Boot Guides](https://spring.io/guides)
- [Oracle Java Documentation](https://docs.oracle.com/javase/21/)

### Best Practices
- Follow RESTful API conventions
- Use proper HTTP status codes
- Implement proper error handling
- Write comprehensive tests
- Use meaningful variable and method names

## 🔧 Troubleshooting

### Common Issues

**Port 8080 already in use:**
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process (replace PID with actual process ID)
kill -9 <PID>
```

**Database connection issues:**
- Check if MySQL is running
- Verify database credentials
- Ensure database exists

**Build failures:**
- Clean and rebuild: `mvn clean install`
- Check Java version: `java --version`
- Verify Maven installation: `mvn --version`

## 🏆 Recent Upgrades

### Java 21 Upgrade (November 2025)
- ✅ **Upgraded from Java 17 to Java 21** (Latest LTS)
- ✅ **Updated Spring Boot** to version 3.3.5 for better Java 21 support
- ✅ **Updated Maven compiler** to target Java 21
- ✅ **All tests passing** with new Java version
- ✅ **Build successful** with Maven 3.9.11

**Benefits of Java 21:**
- **Better Performance**: Improved JVM performance and memory management
- **New Language Features**: Pattern matching, records, text blocks
- **Enhanced Security**: Latest security updates and patches
- **Long-term Support**: Java 21 is an LTS release (supported until 2031)

## Development

### Running Tests
```bash
mvn test
```

### Building for Production
```bash
mvn clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

## Project Structure

```
src/
├── main/
│   ├── java/com/hackme/backend/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # JPA entities
│   │   ├── repository/     # Data repositories
│   │   └── service/        # Business logic
│   └── resources/
│       └── application.properties
└── test/
    ├── java/
    └── resources/
        └── application-test.properties
```