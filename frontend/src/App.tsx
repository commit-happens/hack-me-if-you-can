import "./App.css";
import useNavigate from "./hooks/useNavigate";
import Game from "./pages/game";
import Welcome from "./pages/welcome";

function App() {
  const { page, navigate } = useNavigate();

  const pages = {
    welcome: <Welcome navigate={navigate} />,
    game: <Game navigate={navigate} page={page} />,
  };

  return pages[page];
}

export default App;
