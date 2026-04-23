```markdown
# Copilot Code Review & Repo Instructions

- Při code review používej pro popis nalezeného problému vždy **češtinu**.
- Ignoruj změny formátování kódu, pokud neovlivňují funkčnost.

## Purpose

This repo contains two main parts: a Spring Boot backend (`backend/`) and a React + Vite frontend (`frontend/`). This file tells AI agents how the project is structured, which commands to run, and which conventions to preserve.

## Big-picture architecture
- Backend: Java 21, Spring Boot, Maven. Main class: `cz.hackmeifyoucan.backend.HackMeIfYouCanApplication`.
- Frontend: React 19 + TypeScript, Vite dev server. Key entry: `frontend/src/index.tsx` and pages under `frontend/src/pages`.
- Data flow: Frontend calls backend REST endpoints under `/api/*`. Player DTOs live in `backend/dto/` and are mapped to `entity.Player` via services.

## Project-specific patterns & files to reference
- Controllers: `backend/controller/*` (e.g. `PlayerController.java`).
- DTOs: `backend/dto/*` (requests and responses named `*Request` / `*Response`).
- Exceptions & handlers: `backend/exception/*` and `GlobalExceptionHandler.java` — prefer raising domain exceptions.
- Persistence: `backend/entity/*` + Spring Data JPA repositories in `backend/repository/`.
- Frontend services: `frontend/services/playerService.ts` — keep HTTP paths aligned with backend `/api/players`.
- State: `frontend/store/slices/*` (Redux Toolkit) and hooks in `frontend/store/hooks.ts`.

## Build / run / test (exact commands)
- Run everything with Docker: `docker-compose up --build` (see `docker-compose.yml`).
- Frontend dev: `cd frontend && npm ci && npm run dev` (Vite, port 5173 by default).
- Frontend build: `cd frontend && npm run build`.
- Frontend lint: `cd frontend && npm run lint`.
- Backend dev: from repo root `make be-run` (wraps `cd backend && mvn spring-boot:run`).
- Backend tests: `cd backend && mvn test` or `make be-test`.
- Backend package: `make be-package` (produces `target/*.jar`).

## Integration notes
- H2 in-memory database used for local dev; H2 console available at `/h2-console`.
- Swagger UI (springdoc) available at `/swagger-ui.html` when backend is running.
- Environment variables for frontend: `frontend/.env` (prefix `VITE_`, e.g. `VITE_API_LOCALE`).

## Review & editing guidance for AI agents
- Preserve the Czech review rule: write review messages in Czech.
- When changing APIs, update both backend DTOs/controllers and the frontend `playerService` and affected pages.
- Keep changes minimal and reference concrete files in patch suggestions (small diffs are preferred).
- Run backend tests (`mvn test`) and frontend build/lint (`npm run build`, `npm run lint`) before proposing merges.

If you want this expanded with example PR comments, test running examples, or typical refactor patterns, tell me which area to expand.
```
# Copilot Code Review Instructions

- Při code review používej pro popis nalezeného problému vždy češtinu.
- Ignoruj změny formátování kódu, pokud neovlivňují funkčnost.
