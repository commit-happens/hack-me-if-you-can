#!/bin/bash

# Database Setup Script for Hack Me If You Can
# This script sets up the development database environment
#
# WHAT THIS SCRIPT DOES:
# 1. Checks if Docker is installed and running
# 2. Starts MySQL and phpMyAdmin containers
# 3. Waits for database to be ready
# 4. Creates database schema and sample data
# 5. Provides access URLs and credentials
#
# USAGE:
# chmod +x setup-database.sh
# ./setup-database.sh

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🗄️  Hack Me If You Can - Database Setup${NC}"
echo "=============================================="

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check Docker installation
echo -e "${YELLOW}📋 Checking Docker installation...${NC}"
if ! command_exists docker; then
    echo -e "${RED}❌ Docker is not installed. Please install Docker first.${NC}"
    echo "Visit: https://docs.docker.com/get-docker/"
    exit 1
fi

if ! command_exists docker-compose; then
    echo -e "${RED}❌ Docker Compose is not installed. Please install Docker Compose first.${NC}"
    echo "Visit: https://docs.docker.com/compose/install/"
    exit 1
fi

# Check if Docker daemon is running
if ! docker info >/dev/null 2>&1; then
    echo -e "${RED}❌ Docker daemon is not running. Please start Docker first.${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Docker is installed and running${NC}"

# Navigate to project root
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_ROOT"

echo -e "${YELLOW}📁 Project root: $PROJECT_ROOT${NC}"

# Check if docker-compose.yml exists
if [[ ! -f "docker-compose.yml" ]]; then
    echo -e "${RED}❌ docker-compose.yml not found in project root${NC}"
    exit 1
fi

# Stop any existing containers
echo -e "${YELLOW}🛑 Stopping existing containers...${NC}"
docker-compose down -v 2>/dev/null || true

# Start the database services
echo -e "${YELLOW}🚀 Starting database services...${NC}"
docker-compose up -d mysql

# Wait for MySQL to be ready
echo -e "${YELLOW}⏳ Waiting for MySQL to be ready...${NC}"
max_attempts=30
attempt=1

while ! docker-compose exec -T mysql mysqladmin ping -h localhost --silent; do
    if [[ $attempt -eq $max_attempts ]]; then
        echo -e "${RED}❌ MySQL failed to start within expected time${NC}"
        docker-compose logs mysql
        exit 1
    fi
    echo -e "${YELLOW}   Attempt $attempt/$max_attempts - waiting 2 seconds...${NC}"
    sleep 2
    ((attempt++))
done

echo -e "${GREEN}✅ MySQL is ready!${NC}"

# Start web interfaces
echo -e "${YELLOW}🌐 Starting web interfaces...${NC}"
docker-compose up -d phpmyadmin adminer

# Wait a moment for web interfaces to start
sleep 5

# Check if services are healthy
echo -e "${YELLOW}🏥 Checking service health...${NC}"

if docker-compose ps mysql | grep -q "Up (healthy)"; then
    echo -e "${GREEN}✅ MySQL is healthy${NC}"
else
    echo -e "${YELLOW}⚠️  MySQL health check pending...${NC}"
fi

# Display connection information
echo ""
echo -e "${GREEN}🎉 Database setup completed successfully!${NC}"
echo "=============================================="
echo ""
echo -e "${BLUE}📊 Database Connection Info:${NC}"
echo "   Host: localhost"
echo "   Port: 3306"
echo "   Database: hackme_db"
echo "   Username: hackme_user"
echo "   Password: hackme_password123"
echo "   Root Password: rootpassword123"
echo ""
echo -e "${BLUE}🌐 Web Interfaces:${NC}"
echo "   phpMyAdmin: http://localhost:8081"
echo "   Adminer:    http://localhost:8082"
echo ""
echo -e "${BLUE}🔧 Useful Commands:${NC}"
echo "   Stop services:    docker-compose down"
echo "   View logs:        docker-compose logs -f mysql"
echo "   Reset database:   docker-compose down -v"
echo "   Restart services: docker-compose restart"
echo ""
echo -e "${BLUE}☕ Next Steps:${NC}"
echo "1. Update backend/src/main/resources/application.properties"
echo "2. Change spring.datasource.url to: jdbc:mysql://localhost:3306/hackme_db"  
echo "3. Set spring.datasource.username to: hackme_user"
echo "4. Set spring.datasource.password to: hackme_password123"
echo "5. Start your Spring Boot application: mvn spring-boot:run"
echo ""
echo -e "${GREEN}Happy coding! 🚀${NC}"