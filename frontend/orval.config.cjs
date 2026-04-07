const dotenv = require("dotenv");

dotenv.config({ path: ".env" });

const apiBaseUrl = process.env.VITE_API_BASE_URL ?? "";
const swaggerPath = process.env.VITE_API_SWAGGER_JSON_PATH ?? "/v3/api-docs";

const swaggerUrl = `${apiBaseUrl}${swaggerPath}`;

module.exports = {
  apiQuery: {
    input: {
      target: swaggerUrl,
    },
    output: {
      target: "src/services/generated/query.ts",
      schemas: "src/services/generated/model",
      client: "react-query",
      mode: "tags-split",
      clean: true,
      override: {
        mutator: {
          path: "src/services/httpClient.ts",
          name: "httpClient",
        },
        query: {
          version: 5,
        },
      },
    },
  },
  apiZod: {
    input: {
      target: swaggerUrl,
    },
    output: {
      target: "src/services/generated-zod/zod.ts",
      client: "zod",
      mode: "tags-split",
      clean: false,
    },
  },
};
