import { describe, it, expect } from "vitest";
import { getText } from "./useTranslation";

describe("getText", () => {
  it("vrátí původní text bez hodnot", () => {
    expect(getText("Ahoj")).toBe("Ahoj");
  });

  it("nahradí placeholdery {1}, {2} správnými hodnotami", () => {
    expect(getText("Hra {1}/{2}", [3, 10])).toBe("Hra 3/10");
  });

  it("převede čísla i stringy na text", () => {
    expect(getText("{1}-{2}-{3}", ["A", 7, "B"])).toBe("A-7-B");
  });

  it("nezmění text pokud placeholders nemají odpovídající hodnoty", () => {
    expect(getText("X {1} Y {2}", [5])).toBe("X 5 Y {2}");
  });
});
