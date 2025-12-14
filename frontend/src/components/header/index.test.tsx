import { describe, it, expect } from "vitest";
import { MemoryRouter } from "react-router-dom";
import { render, screen } from "@testing-library/react";
import Header from "./index";

// Pomocná funkce pro render s danou cestou
function renderHeader(path: string) {
  return render(
    <MemoryRouter initialEntries={[path]}>
      <Header />
    </MemoryRouter>,
  );
}

describe("Header", () => {
  it("zobrazí odkaz na ostatních stránkách (/game)", () => {
    renderHeader("/game");
    const text = screen.getByText("Hack me if you can");
    const link = text.closest("a");
    expect(link).toBeInTheDocument();
    expect(link).toHaveAttribute("href", "/");
  });

  it("zobrazí odkaz na stránce /results", () => {
    renderHeader("/results");
    const text = screen.getByText("Hack me if you can");
    const link = text.closest("a");
    expect(link).not.toBeNull();
    expect(link).toHaveAttribute("href", "/");
  });
});
