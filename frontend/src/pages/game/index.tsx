import Header from "../../components/header";
import type { BasePageProps } from "../../models/BasePageProps";

function Game(props: BasePageProps) {
  const { page, navigate, playerHook } = props;
  
  // Display player information if available
  const playerName = playerHook?.playerName || 'Unknown Player';
  const playerScore = playerHook?.playerScore || 0;
  
  return (
    <>
      <Header navigate={navigate} page={page} />
      <h1>Game Page</h1>
      
      {/* Player Information */}
      <div style={{ 
        backgroundColor: '#f5f5f5', 
        padding: '20px', 
        borderRadius: '8px', 
        marginBottom: '20px' 
      }}>
        <h2>👋 Vítej, {playerName}!</h2>
        <p>Aktuální skóre: <strong>{playerScore} bodů</strong></p>
        <p>✅ Úspěšně připojen k databázi!</p>
      </div>
      
      <p>TODO: Implement the game page.</p>
      <p>Zde bude hra na rozpoznávání phishingových e-mailů.</p>
    </>
  );
}

export default Game;
