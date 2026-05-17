import { useEffect, useState } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { Provider } from "react-redux";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { routes } from "./routing/routes";
import { store } from "./store";

function createQueryClient() {
  return new QueryClient();
}

type AppQueryClient = ReturnType<typeof createQueryClient>;

declare global {
  var __HMIYC_QUERY_CLIENT__: AppQueryClient | undefined;
}

function getQueryClient() {
  if (import.meta.env.DEV) {
    if (!globalThis.__HMIYC_QUERY_CLIENT__) {
      globalThis.__HMIYC_QUERY_CLIENT__ = createQueryClient();
    }

    return globalThis.__HMIYC_QUERY_CLIENT__;
  }

  return createQueryClient();
}

const queryClient = getQueryClient();

function App() {
  const [locale] = useState("cs");

  useEffect(() => {
    // Nastaví správný kód jazyka do <html lang="...">
    document.documentElement.lang = locale;
  }, [locale]);

  return (
    <QueryClientProvider client={queryClient}>
      <Provider store={store}>
        <BrowserRouter>
          <Routes>
            {Object.values(routes).map(({ path, element }) => (
              <Route key={path} path={path} element={element} />
            ))}
          </Routes>
        </BrowserRouter>
      </Provider>
    </QueryClientProvider>
  );
}

export default App;
