import { describe, it, expect } from "vitest";
import { MemoryRouter } from "react-router-dom";
import { render, screen } from "@testing-library/react";
import Header from "./index";

// Pomocná funkce pro render s danou cestou
function renderHeader(path: string) {
  return render(
    <MemoryRouter initialEntries={[path]}>
      <Header />
    </MemoryRouter>
  );
}

describe("Header", () => {
  it("zobrazí pouze text na úvodní stránce /", () => {
    renderHeader("/");
    const text = screen.getByText("HMIYC");
    expect(text).toBeInTheDocument();
    // Neměl by být link, takže closest('a') je null
    expect(text.closest("a")).toBeNull();
  });

  it("zobrazí odkaz na ostatních stránkách (/game)", () => {
    renderHeader("/game");
    const link = screen.getByRole("link", { name: "HMIYC" });
    expect(link).toBeInTheDocument();
    expect(link).toHaveAttribute("href", "/");
  });

  it("zobrazí odkaz na stránce /results", () => {
    renderHeader("/results");
    const link = screen.getByRole("link", { name: "HMIYC" });
    expect(link).toBeInTheDocument();
    expect(link).toHaveAttribute("href", "/");
  });
});
