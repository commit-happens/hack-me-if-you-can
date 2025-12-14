import { afterEach } from "vitest";
import { cleanup } from "@testing-library/react";
import "@testing-library/jest-dom";

// Automatický cleanup po každém testu
afterEach(() => {
  cleanup();
});
