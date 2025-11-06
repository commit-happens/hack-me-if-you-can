import { useEffect, useState } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./App.css";
import { routes } from "./routing/routes";

function App() {
  const [locale] = useState("cs");

  useEffect(() => {
    // Nastaví správný kód jazyka do <html lang="...">
    document.documentElement.lang = locale;
  }, [locale]);

  return (
    <BrowserRouter>
      <Routes>
        {Object.values(routes).map(({ path, element }) => (
          <Route key={path} path={path} element={element} />
        ))}
      </Routes>
    </BrowserRouter>
  );
}

export default App;
