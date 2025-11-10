import { useEffect, useState } from 'react';
import './App.css';
import useNavigate from './hooks/useNavigate';
import Game from './pages/game';
import Welcome from './pages/welcome';

function App() {
  const { page, navigate } = useNavigate();
  const [locale] = useState('cs');

  const pages = {
    welcome: <Welcome navigate={navigate} />,
    game: <Game navigate={navigate} page={page} />,
  };

  useEffect(() => {
    // Nastaví správný kód jazyka do <html lang="...">
    document.documentElement.lang = locale;
  }, [locale]);

  return pages[page];
}

export default App;
