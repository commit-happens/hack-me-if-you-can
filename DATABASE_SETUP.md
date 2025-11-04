# Database Setup Guide 🗄️

This guide provides multiple ways to set up MySQL database for the "Hack Me If You Can" project without needing to navigate to MySQL Workbench or phpMyAdmin UI.

## 📋 Table of Contents

- [Quick Start with Docker](#quick-start-with-docker-recommended)
- [Manual MySQL Setup](#manual-mysql-setup)
- [Development vs Production](#development-vs-production)
- [Database Schema](#database-schema)
- [Troubleshooting](#troubleshooting)

## 🐳 Quick Start with Docker (Recommended)

### Why Docker?
- **No MySQL installation needed** - runs in container
- **Consistent environment** - same setup for all developers
- **Easy cleanup** - remove container when done
- **Version control** - database config in repository

### 1. Start Database with Docker Compose

```bash
# Navigate to project root
cd /Users/eliska.kremenova/Projects/hack-me-if-you-can

# Start MySQL database in background
docker-compose up -d database

# Check if database is running
docker-compose ps
```

### 2. Connect to Database

**Database Connection Details:**
- **Host**: `localhost`
- **Port**: `3306`
- **Database**: `hackme_db`
- **Username**: `hackme_user`
- **Password**: `hackme_password`
- **Root Password**: `root_password`

### 3. Access Database via Command Line

```bash
# Connect as regular user
docker exec -it hack-me-if-you-can-database-1 mysql -u hackme_user -p hackme_db

# Connect as root user
docker exec -it hack-me-if-you-can-database-1 mysql -u root -p

# View database logs
docker-compose logs database
```

### 4. Initialize Database Schema

```bash
# Database schema is automatically created from init scripts
# Check if tables were created:
docker exec -it hack-me-if-you-can-database-1 mysql -u hackme_user -p hackme_db -e "SHOW TABLES;"
```

## 🔧 Manual MySQL Setup

If you prefer installing MySQL directly on your system:

### Option 1: Using Setup Scripts

```bash
# For macOS/Linux
./scripts/setup-database.sh

# For Windows
.\scripts\setup-database.bat
```

### Option 2: Manual Commands

```bash
# Install MySQL (macOS with Homebrew)
brew install mysql
brew services start mysql

# Create database and user
mysql -u root -p << EOF
CREATE DATABASE hackme_db;
CREATE USER 'hackme_user'@'localhost' IDENTIFIED BY 'hackme_password';
GRANT ALL PRIVILEGES ON hackme_db.* TO 'hackme_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
EOF

# Initialize schema
mysql -u hackme_user -p hackme_db < database/init/01_create_schema.sql
```

## 🏗️ Development vs Production

### Development Configuration

The application uses **H2 in-memory database** by default for development:

```properties
# application.properties (current setup)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=create-drop
```

**Advantages:**
- ✅ No setup required
- ✅ Fast startup
- ✅ Automatic schema creation
- ✅ Perfect for testing

**Access H2 Console:** `http://localhost:8080/h2-console`

### Production Configuration

For production, switch to MySQL by updating `application.properties`:

```properties
# MySQL Production Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/hackme_db
spring.datasource.username=hackme_user
spring.datasource.password=hackme_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.show-sql=false
```

### Profile-based Configuration

Create separate configuration files:

**application-dev.properties** (Development):
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.h2.console.enabled=true
spring.jpa.show-sql=true
```

**application-prod.properties** (Production):
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hackme_db
spring.datasource.username=hackme_user
spring.datasource.password=${DB_PASSWORD:hackme_password}
spring.jpa.show-sql=false
```

Run with specific profile:
```bash
# Development
mvn spring-boot:run -Dspring.profiles.active=dev

# Production
mvn spring-boot:run -Dspring.profiles.active=prod
```

## 📊 Database Schema

### Current Tables

#### Players Table
```sql
CREATE TABLE players (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nickname VARCHAR(50) NOT NULL UNIQUE,
    score INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Sample Data

```sql
-- Insert sample players
INSERT INTO players (nickname, score) VALUES 
('security_pro', 95),
('phishing_hunter', 87),
('cyber_newbie', 23),
('email_detective', 78);
```

## 🔍 Database Management Commands

### Useful MySQL Commands

```bash
# Show all databases
mysql -u hackme_user -p -e "SHOW DATABASES;"

# Show all tables in hackme_db
mysql -u hackme_user -p hackme_db -e "SHOW TABLES;"

# Describe players table structure
mysql -u hackme_user -p hackme_db -e "DESCRIBE players;"

# View all players
mysql -u hackme_user -p hackme_db -e "SELECT * FROM players;"

# Check player count
mysql -u hackme_user -p hackme_db -e "SELECT COUNT(*) as player_count FROM players;"

# Find top players
mysql -u hackme_user -p hackme_db -e "SELECT nickname, score FROM players ORDER BY score DESC LIMIT 10;"
```

### Backup and Restore

```bash
# Create backup
mysqldump -u hackme_user -p hackme_db > backup_$(date +%Y%m%d).sql

# Restore from backup
mysql -u hackme_user -p hackme_db < backup_20251103.sql
```

## 🐛 Troubleshooting

### Common Issues

#### 1. Port 3306 Already in Use
```bash
# Find what's using port 3306
lsof -i :3306

# Stop existing MySQL service
brew services stop mysql
# or
sudo systemctl stop mysql
```

#### 2. Docker Container Won't Start
```bash
# Check Docker logs
docker-compose logs database

# Remove old containers
docker-compose down
docker system prune -f

# Restart fresh
docker-compose up -d database
```

#### 3. Connection Refused
```bash
# Check if MySQL is running
docker-compose ps
# or
brew services list | grep mysql

# Check network connectivity
telnet localhost 3306
```

#### 4. Authentication Issues
```bash
# Reset MySQL password (Docker)
docker-compose exec database mysql -u root -p -e "ALTER USER 'hackme_user'@'%' IDENTIFIED BY 'hackme_password';"

# Reset MySQL password (Local)
mysql -u root -p -e "ALTER USER 'hackme_user'@'localhost' IDENTIFIED BY 'hackme_password';"
```

#### 5. Spring Boot Can't Connect
- Check `application.properties` database URL
- Verify username/password
- Ensure database exists
- Check if MySQL is running on correct port

### Health Checks

```bash
# Test database connection
mysql -u hackme_user -p hackme_db -e "SELECT 1 as test;"

# Check Spring Boot database connection
curl http://localhost:8080/actuator/health

# Verify H2 console (development)
curl http://localhost:8080/h2-console
```

## 🚀 Quick Commands Reference

```bash
# Start everything with Docker
docker-compose up -d

# Stop everything
docker-compose down

# View logs
docker-compose logs -f database

# Connect to database
docker exec -it hack-me-if-you-can-database-1 mysql -u hackme_user -p hackme_db

# Run Spring Boot with MySQL
mvn spring-boot:run -Dspring.profiles.active=prod

# Test API with database
curl http://localhost:8080/api/players
```

## 📚 Additional Resources

- [MySQL Official Documentation](https://dev.mysql.com/doc/)
- [Spring Boot Database Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/data.html)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [H2 Database Documentation](http://h2database.com/html/main.html)