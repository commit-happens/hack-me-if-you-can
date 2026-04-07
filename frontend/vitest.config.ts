import { defineConfig } from "vitest/config";

export default defineConfig({
  test: {
    globals: true,
    environment: "jsdom",
    setupFiles: "./src/tests/setup.ts",
    include: ["src/**/*.test.ts?(x)"],
    api: {
      host: "127.0.0.1",
      port: 51304,
      strictPort: true,
    },
  },
});
