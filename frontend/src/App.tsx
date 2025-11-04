import "./App.css";
import useNavigate from "./hooks/useNavigate";
import usePlayer from "./hooks/usePlayer";
import Game from "./pages/game";
import Welcome from "./pages/welcome";
import ApiTest from "./components/ApiTest";

function App() {
  const { page, navigate } = useNavigate();
  const playerHook = usePlayer();

  const pages = {
    welcome: <Welcome navigate={navigate} playerHook={playerHook} />,
    game: <Game navigate={navigate} page={page} playerHook={playerHook} />,
  };

  // Show API Test Console only in development
  const showApiTest = import.meta.env.DEV; // Vite development mode

  return (
    <>
      {pages[page]}
      {/* API Test Component - only in development */}
      {showApiTest && <ApiTest />}
    </>
  );
}

export default App;
