import { describe, it, expect } from "vitest";
import { getText } from "./useTranslation";

describe("getText", () => {
  it("Nahradí placeholdery {1}, {2} správnými hodnotami", () => {
    expect(getText("Hra {1}/{2}", [3, 10])).toBe("Hra 3/10");
  });

  it("Převede čísla i stringy na text", () => {
    expect(getText("{1}-{2}-{3}", ["A", 7, "B"])).toBe("A-7-B");
  });

  it("Nezmění text, pokud placeholdery neobsahují odpovídající hodnoty", () => {
    expect(getText("X {1} Y {2}", [5])).toBe("X 5 Y {2}");
  });
});
