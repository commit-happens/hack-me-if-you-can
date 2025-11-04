import { useState } from "react";
import { PlayerService } from "../services/playerService";

/**
 * API Test Component - Debug tool to test backend connection
 * 
 * This component provides a simple interface to test all API endpoints
 * and verify that frontend-backend communication is working correctly.
 */
function ApiTest() {
  const [results, setResults] = useState<string[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [testNickname, setTestNickname] = useState<string>('');
  const [lastCreatedPlayer, setLastCreatedPlayer] = useState<string>('');

  const addResult = (message: string) => {
    setResults(prev => [...prev, `${new Date().toLocaleTimeString()}: ${message}`]);
  };

  const clearResults = () => {
    setResults([]);
  };

  const testCreatePlayer = async () => {
    setIsLoading(true);
    try {
      const randomNickname = `test_${Date.now()}`;
      const result = await PlayerService.createPlayer(randomNickname);
      setLastCreatedPlayer(randomNickname); // Remember the last created player
      addResult(`✅ Player created: ${JSON.stringify(result)}`);
    } catch (error) {
      addResult(`❌ Create failed: ${error instanceof Error ? error.message : 'Unknown error'}`);
    }
    setIsLoading(false);
  };

  const testGetPlayer = async () => {
    const nickname = testNickname.trim() || lastCreatedPlayer || 'hackergame123';
    setIsLoading(true);
    try {
      const result = await PlayerService.getPlayerScore(nickname);
      addResult(`✅ Player found (${nickname}): ${JSON.stringify(result)}`);
    } catch (error) {
      addResult(`❌ Get failed (${nickname}): ${error instanceof Error ? error.message : 'Unknown error'}`);
    }
    setIsLoading(false);
  };

  const testPlayerExists = async () => {
    const nickname = testNickname.trim() || lastCreatedPlayer || 'hackergame123';
    setIsLoading(true);
    try {
      const exists = await PlayerService.playerExists(nickname);
      addResult(`✅ Player exists check (${nickname}): ${exists}`);
    } catch (error) {
      addResult(`❌ Exists check failed (${nickname}): ${error instanceof Error ? error.message : 'Unknown error'}`);
    }
    setIsLoading(false);
  };

  const testConnection = async () => {
    setIsLoading(true);
    addResult('🔄 Starting connection test...');
    
    try {
      // Test backend health
      const response = await fetch('http://localhost:8080/api/players/healthcheck/exists');
      if (response.ok) {
        addResult('✅ Backend is reachable');
      } else {
        addResult(`⚠️ Backend responded with status: ${response.status}`);
      }
    } catch (error) {
      addResult('❌ Cannot connect to backend - is it running on port 8080?');
    }
    
    setIsLoading(false);
  };

  return (
    <div style={{ 
      position: 'fixed', 
      top: '10px', 
      right: '10px', 
      width: '300px', 
      padding: '15px',
      backgroundColor: '#f0f0f0',
      border: '1px solid #ccc',
      borderRadius: '8px',
      fontSize: '12px',
      maxHeight: '400px',
      overflow: 'auto',
      zIndex: 1000
    }}>
      <h3 style={{ margin: '0 0 10px 0' }}>🔧 API Test Console</h3>
      
      {/* Nickname input for testing specific players */}
      <div style={{ marginBottom: '10px' }}>
        <input
          type="text"
          placeholder="Enter nickname to test (or use last created)"
          value={testNickname}
          onChange={(e) => setTestNickname(e.target.value)}
          style={{
            width: '100%',
            padding: '4px',
            fontSize: '11px',
            marginBottom: '5px',
            border: '1px solid #ccc',
            borderRadius: '3px'
          }}
        />
        {lastCreatedPlayer && (
          <div style={{ fontSize: '10px', color: '#666', marginBottom: '5px' }}>
            Last created: {lastCreatedPlayer}
          </div>
        )}
      </div>
      
      <div style={{ marginBottom: '10px' }}>
        <button onClick={testConnection} disabled={isLoading} style={{ marginRight: '5px', fontSize: '10px', padding: '2px 6px' }}>
          Test Connection
        </button>
        <button onClick={testCreatePlayer} disabled={isLoading} style={{ marginRight: '5px', fontSize: '10px', padding: '2px 6px' }}>
          Create Random
        </button>
        <button onClick={testGetPlayer} disabled={isLoading} style={{ marginRight: '5px', fontSize: '10px', padding: '2px 6px' }}>
          Get Player
        </button>
        <button onClick={testPlayerExists} disabled={isLoading} style={{ marginRight: '5px', fontSize: '10px', padding: '2px 6px' }}>
          Check Exists
        </button>
        <button onClick={clearResults} disabled={isLoading} style={{ fontSize: '10px', padding: '2px 6px' }}>
          Clear
        </button>
      </div>

      <div style={{
        backgroundColor: '#1e1e1e',
        color: '#00ff00',
        padding: '10px',
        fontFamily: 'monospace',
        fontSize: '10px',
        maxHeight: '250px',
        overflowY: 'auto',
        borderRadius: '4px'
      }}>
        {results.length === 0 ? (
          <div style={{ color: '#888' }}>No tests run yet...</div>
        ) : (
          results.map((result, index) => (
            <div key={index} style={{ marginBottom: '5px' }}>
              {result}
            </div>
          ))
        )}
      </div>
      
      <div style={{ marginTop: '10px', fontSize: '10px', color: '#666' }}>
        💡 Use this to verify API connection while developing
      </div>
    </div>
  );
}

export default ApiTest;