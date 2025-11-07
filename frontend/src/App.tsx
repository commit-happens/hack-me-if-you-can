import { useEffect, useState } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { Provider } from "react-redux";
import "./App.css";
import { routes } from "./routing/routes";
import { store } from "./store";

function App() {
  const [locale] = useState("cs");

  useEffect(() => {
    // Nastaví správný kód jazyka do <html lang="...">
    document.documentElement.lang = locale;
  }, [locale]);

  return (
    <Provider store={store}>
      <BrowserRouter>
        <Routes>
          {Object.values(routes).map(({ path, element }) => (
            <Route key={path} path={path} element={element} />
          ))}
        </Routes>
      </BrowserRouter>
    </Provider>
  );
}

export default App;
