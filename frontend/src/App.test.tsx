import { render } from "@testing-library/react";
import { describe, it, expect } from "vitest";
import App from "./App";

describe("App", () => {
  it("Aplikace běží!", () => {
    render(<App />);
    // Základní test, že aplikace běží (vykreslí se do DOM)
    expect(document.body).toBeTruthy();
  });
});
