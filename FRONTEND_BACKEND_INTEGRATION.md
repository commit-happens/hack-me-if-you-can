# Frontend-Backend Integration Guide 🔗

This document explains how the React frontend connects to the Spring Boot backend in the "Hack Me If You Can" project.

## 🎯 Quick Start

### 1. Start the Backend
```bash
cd backend
mvn spring-boot:run
```
Backend will run on: `http://localhost:8080`

### 2. Start the Frontend
```bash
cd frontend
npm run dev
```
Frontend will run on: `http://localhost:5173`

### 3. Test the Connection
1. Open the frontend in your browser
2. Enter a nickname (at least 3 characters)
3. Click "Start!" - this will create a player in the database
4. You should see the player information on the game page

## 🏗️ Architecture Overview

```
React Frontend (Port 5173)
           ↓ HTTP Requests
    Spring Boot API (Port 8080)
           ↓ JPA/Hibernate
      H2 Database (In-Memory)
```

## 📡 API Integration

### Player Service (`frontend/src/services/playerService.ts`)
The main API communication layer that handles all HTTP requests to the backend:

- **CREATE Player**: `POST /api/players/score` - Creates new player with nickname and score 0
- **GET Player**: `GET /api/players/{nickname}/score` - Retrieves player data
- **UPDATE Score**: `POST /api/players/score` - Updates player's score
- **CHECK Exists**: `GET /api/players/{nickname}/exists` - Verifies if player exists
- **DELETE Player**: `DELETE /api/players/{nickname}/score` - Removes player

### Player Hook (`frontend/src/hooks/usePlayer.ts`)
React hook that manages player state and provides methods for:
- Creating players with loading states and error handling
- Updating scores during gameplay
- Managing player data across components

## 🔄 Data Flow

### Player Creation Flow
```
1. User enters nickname in Welcome page
2. handleStart() calls createPlayer(nickname)
3. PlayerService.createPlayer() sends POST request
4. Backend creates Player entity in database
5. Backend returns PlayerScoreResponse
6. Frontend navigates to Game page
7. Game page displays player information
```

### API Request Example
```typescript
// Frontend creates player
const response = await fetch('http://localhost:8080/api/players/score', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    nickname: "hackergame123",
    score: 0
  })
});

// Backend responds with:
{
  "nickname": "hackergame123",
  "score": 0,
  "message": "Player created successfully"
}
```

## 🚦 CORS Configuration

The backend is configured to accept requests from the frontend:

```java
// WebConfig.java
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:5173", "http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
}
```

## 🎮 Component Integration

### Welcome Page (`frontend/src/pages/welcome/index.tsx`)
- Collects player nickname
- Validates input (3-50 characters)
- Creates player via API call
- Shows loading states and error messages
- Navigates to game on success

### Game Page (`frontend/src/pages/game/index.tsx`)
- Displays player information from database
- Shows current score
- Ready for game logic implementation

### API Test Component (`frontend/src/components/ApiTest.tsx`)
- Debug console for testing API endpoints
- Visible in top-right corner during development
- Test connection, create players, verify responses

## 🗄️ Database Integration

### Development Setup (H2)
- **In-memory database** - data resets on restart
- **Auto-schema creation** - tables created automatically
- **H2 Console**: `http://localhost:8080/api/h2-console`
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (empty)

### Check Database Content
```sql
-- View all players
SELECT * FROM players;

-- Count players
SELECT COUNT(*) as player_count FROM players;

-- Find specific player
SELECT * FROM players WHERE nickname = 'hackergame123';
```

## 🧪 Testing the Integration

### Manual Testing
1. **API Test Script**: Run `./backend/test-api.sh`
2. **Frontend Form**: Use the welcome page form
3. **API Test Component**: Use the debug console in browser
4. **H2 Console**: Check database directly

### Automated Testing
```bash
# Backend tests
cd backend
mvn test

# Frontend tests (when implemented)
cd frontend
npm test
```

## 🔧 Troubleshooting

### Common Issues

#### 1. CORS Errors
```
Access to fetch at 'http://localhost:8080/api/players/score' from origin 'http://localhost:5173' has been blocked by CORS policy
```
**Solution**: Check that backend WebConfig includes frontend URL

#### 2. Connection Refused
```
Failed to fetch: TypeError: Failed to fetch
```
**Solutions**:
- Ensure backend is running: `mvn spring-boot:run`
- Check backend URL in playerService.ts
- Verify port 8080 is not blocked

#### 3. 404 Not Found
```
GET http://localhost:8080/api/players/test/score 404 (Not Found)
```
**Solutions**:
- Check API endpoint URLs match controller mappings
- Verify player exists in database
- Check server.servlet.context-path=/api in application.properties

#### 4. Validation Errors
```
{
  "message": "Validation failed",
  "errors": {
    "nickname": "Nickname must be between 3 and 50 characters"
  }
}
```
**Solution**: Check input validation in frontend and backend DTOs

### Debug Steps
1. **Check Backend Logs**: Look for errors in Spring Boot console
2. **Check Network Tab**: Inspect HTTP requests in browser dev tools
3. **Test API Directly**: Use curl or Postman to test endpoints
4. **Check Database**: Use H2 console to verify data persistence

## 🚀 Next Steps

### Planned Enhancements
1. **Game Logic Integration**: Connect scoring system to actual game
2. **Leaderboards**: Display top players from database
3. **Sessions**: Persist player state across browser sessions
4. **Real-time Updates**: WebSocket integration for live scoring

### Production Considerations
1. **Remove ApiTest Component**: Not needed in production
2. **Environment Variables**: Configure API URLs for different environments
3. **Error Boundaries**: Add React error boundaries for better UX
4. **Loading States**: Enhance UI feedback during API calls

## 📚 Key Files

### Frontend
- `src/services/playerService.ts` - API communication layer
- `src/hooks/usePlayer.ts` - React state management for players
- `src/pages/welcome/index.tsx` - Player creation form
- `src/pages/game/index.tsx` - Game page with player display
- `src/components/ApiTest.tsx` - Debug console (development only)

### Backend
- `src/main/java/com/hackme/backend/controller/PlayerController.java` - REST endpoints
- `src/main/java/com/hackme/backend/service/PlayerService.java` - Business logic
- `src/main/java/com/hackme/backend/config/WebConfig.java` - CORS configuration
- `test-api.sh` - API testing script

## ✅ Success Criteria

Your frontend-backend integration is working correctly when:
- ✅ User can enter nickname and create player
- ✅ Player data is stored in H2 database
- ✅ Game page displays player information from database
- ✅ API test console shows successful connections
- ✅ No CORS errors in browser console
- ✅ Backend logs show successful API calls

**🎉 Congratulations! Your full-stack application is now connected!**