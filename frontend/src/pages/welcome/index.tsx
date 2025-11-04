import { useState } from "react";
import Header from "../../components/header";
import useTranslation from "../../hooks/useTranslation";
import usePlayer from "../../hooks/usePlayer";
import type { BasePageProps } from "../../models/BasePageProps";
import Page from "../../models/Page";

function Welcome(props: BasePageProps) {
  const { navigate, page, playerHook } = props;
  const [nickname, setNickname] = useState<string>("");
  const [validationError, setValidationError] = useState<string>("");
  
  // Use player hook from props or create local one if not provided
  const localPlayerHook = usePlayer();
  const { createPlayer, isLoading, error: apiError, clearError } = playerHook || localPlayerHook;

  const appTexts = useTranslation("app");
  const welcomeTexts = useTranslation("welcome");

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value;
    setNickname(value);
    
    // Clear previous errors when user types
    setValidationError("");
    if (apiError) {
      clearError();
    }
    
    // Validate nickname format
    if (value.length > 0 && value.length < 3) {
      setValidationError("Přezdívka musí mít alespoň 3 znaky");
    } else if (value.length > 50) {
      setValidationError("Přezdívka může mít maximálně 50 znaků");
    }
  };

  const handleStart = async () => {
    if (!navigate) return;
    
    // Final validation
    const trimmedNickname = nickname.trim();
    if (trimmedNickname.length < 3) {
      setValidationError("Přezdívka musí mít alespoň 3 znaky");
      return;
    }
    
    try {
      // Create player in database
      await createPlayer(trimmedNickname);
      
      // If successful, navigate to game
      navigate(Page.Game);
    } catch (error) {
      // Error is already handled by usePlayer hook
      console.error("Failed to create player:", error);
    }
  };

  const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === "Enter" && !hasError) {
      handleStart();
    }
  };
  
  // Combined error checking
  const hasError = validationError !== "" || nickname.trim().length < 3;
  return (
    <>
      <Header navigate={navigate} page={page} />

      <h1>{appTexts.title}</h1>
      <div>
        <p>{welcomeTexts.welcomeMessage}</p>
        <p>{welcomeTexts.instruction1}</p>
        <p>{welcomeTexts.instruction2}</p>
        <div>
          <div>
            <h2>{welcomeTexts.nicknameLabel}</h2>
          </div>
          <p>
            <input
              type="text"
              id="nickname"
              name="nickname"
              placeholder={welcomeTexts.nicknamePlaceholder}
              autoFocus
              value={nickname}
              required
              onChange={handleChange}
              onKeyDown={handleKeyDown}
              disabled={isLoading}
            />
          </p>
          {/* Error messages */}
          {validationError && (
            <p style={{ color: 'red', fontSize: '0.9em' }}>
              {validationError}
            </p>
          )}
          {apiError && (
            <p style={{ color: 'red', fontSize: '0.9em' }}>
              Chyba: {apiError}
            </p>
          )}
        </div>
        <button 
          disabled={hasError || isLoading} 
          onClick={handleStart}
          style={{ 
            opacity: hasError || isLoading ? 0.6 : 1,
            cursor: hasError || isLoading ? 'not-allowed' : 'pointer'
          }}
        >
          {isLoading ? 'Vytváření...' : welcomeTexts.startButton}
        </button>
      </div>
    </>
  );
}

export default Welcome;
