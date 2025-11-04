#!/bin/bash

# Colors for better output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

echo -e "${BLUE}🚀 Testing Hack Me If You Can API${NC}"
echo "=============================================="
echo -e "${YELLOW}Testing backend API endpoints...${NC}"
echo ""

# Check if backend is running
echo -e "${YELLOW}🔍 Checking if backend is running...${NC}"
if curl -s http://localhost:8080/api/players > /dev/null 2>&1; then
    echo -e "${GREEN}✅ Backend is running on http://localhost:8080${NC}"
else
    echo -e "${RED}❌ Backend is not running! Please start with: mvn spring-boot:run${NC}"
    exit 1
fi
echo ""

# Test 1: Create a player (frontend scenario)
echo -e "${PURPLE}📝 Test 1: Creating new player 'hackergame123'...${NC}"
response1=$(curl -s -w "HTTP_STATUS:%{http_code}" -X POST http://localhost:8080/api/players/score \
  -H "Content-Type: application/json" \
  -d '{"nickname": "hackergame123", "score": 0}')

http_status=$(echo "$response1" | grep -o "HTTP_STATUS:[0-9]*" | cut -d: -f2)
response_body=$(echo "$response1" | sed 's/HTTP_STATUS:[0-9]*$//')

if [ "$http_status" = "200" ]; then
    echo -e "${GREEN}✅ Success!${NC} Response: $response_body"
else
    echo -e "${RED}❌ Failed (HTTP $http_status)${NC} Response: $response_body"
fi
echo ""

# Test 2: Check if player exists (frontend validation)
echo -e "${PURPLE}✅ Test 2: Checking if player exists...${NC}"
response2=$(curl -s http://localhost:8080/api/players/hackergame123/exists)
echo -e "${GREEN}Response:${NC} $response2"
echo ""

# Test 3: Get player score (game display)
echo -e "${PURPLE}📖 Test 3: Getting player score...${NC}"
response3=$(curl -s http://localhost:8080/api/players/hackergame123/score)
echo -e "${GREEN}Response:${NC} $response3"
echo ""

# Test 4: Update player score (game progression)
echo -e "${PURPLE}🔄 Test 4: Updating player score to 85 (after answering questions)...${NC}"
response4=$(curl -s -X POST http://localhost:8080/api/players/score \
  -H "Content-Type: application/json" \
  -d '{"nickname": "hackergame123", "score": 85}')
echo -e "${GREEN}Response:${NC} $response4"
echo ""

# Test 5: Get updated score
echo -e "${PURPLE}📊 Test 5: Getting updated score...${NC}"
response5=$(curl -s http://localhost:8080/api/players/hackergame123/score)
echo -e "${GREEN}Response:${NC} $response5"
echo ""

# Test 6: Create another player (multiple users scenario)
echo -e "${PURPLE}👥 Test 6: Creating second player 'cyberdetective'...${NC}"
response6=$(curl -s -X POST http://localhost:8080/api/players/score \
  -H "Content-Type: application/json" \
  -d '{"nickname": "cyberdetective", "score": 42}')
echo -e "${GREEN}Response:${NC} $response6"
echo ""

# Test 7: Test validation (invalid nickname)
echo -e "${PURPLE}⚠️  Test 7: Testing validation with short nickname...${NC}"
response7=$(curl -s -w "HTTP_STATUS:%{http_code}" -X POST http://localhost:8080/api/players/score \
  -H "Content-Type: application/json" \
  -d '{"nickname": "ab", "score": 10}')

http_status7=$(echo "$response7" | grep -o "HTTP_STATUS:[0-9]*" | cut -d: -f2)
response_body7=$(echo "$response7" | sed 's/HTTP_STATUS:[0-9]*$//')

if [ "$http_status7" = "400" ]; then
    echo -e "${GREEN}✅ Validation working correctly!${NC} (HTTP 400 expected)"
    echo -e "${YELLOW}Response:${NC} $response_body7"
else
    echo -e "${RED}❌ Validation not working (Expected HTTP 400, got $http_status7)${NC}"
fi
echo ""

echo -e "${GREEN}🎉 API testing complete!${NC}"
echo "=============================================="
echo -e "${BLUE}💡 Next steps:${NC}"
echo "1. 🌐 Open frontend: cd frontend && npm run dev"
echo "2. 🎮 Open browser: http://localhost:5173"
echo "3. 🗄️  Check H2 Database: http://localhost:8080/api/h2-console"
echo "   � Query: SELECT * FROM players;"
echo ""
echo -e "${YELLOW}📊 Database Access Info:${NC}"
echo "   🔗 JDBC URL: jdbc:h2:mem:testdb" 
echo "   👤 Username: sa"
echo "   🔐 Password: (leave empty)"
echo ""
echo -e "${GREEN}✨ Ready for frontend testing!${NC}"