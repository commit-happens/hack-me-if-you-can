# CLAUDE.md - Frontend kontext a pravidla práce

Projekt: hack-me-if-you-can

Tento soubor je operativní návod pro AI asistenta a vývojáře při úpravách ve složce frontend.
Používej jej jako rychlý checklist před změnami, ne jako náhradu README.

## 1) Scope

- Platí pro frontend část projektu ve složce frontend.
- Primární stack: React 19 + TypeScript + Vite.
- Stav aplikace: Redux Toolkit.
- Routing: react-router-dom v7.
- API vrstva: ky + Orval (OpenAPI) + TanStack Query v5.
- Testy: Vitest + Testing Library (jsdom).

## 2) Rychlý start

Spouštět z frontend:

```powershell
npm ci
npm run dev
```

Alternativa z rootu repozitáře:

```powershell
make fe-sync-deps
make fe-dev
```

## 3) Důležité příkazy

- `npm run dev` - lokální vývojový server.
- `npm run build` - TypeScript build + Vite build.
- `npm run lint` - ESLint kontrola.
- `npm run test` - Vitest.
- `npm run test:watch` - Vitest UI režim.
- `npm run test:coverage` - testy s coverage.
- `npm run api:generate` - generování API klienta a zod schémat z OpenAPI.
- `npm run test:api-contract` - contract testy pro generated API.
- `npm run api:verify` - generate + contract testy (doporučeno před PR).

## 3.1) Auto-format hook

- Po editaci souboru se automaticky spouští Prettier a ESLint pomocí Node hooku.
- Hook se spouští jen po akcích Write/Edit (ne vždy po patchi), takže ruční editace nebo některé automatické úpravy nemusí hook vyvolat.
- Konfigurace hooku je v [frontend/.github/hooks/auto-format-hook.json](.github/hooks/auto-format-hook.json) a spouštěný skript je [frontend/.github/hooks/auto-format.cjs](.github/hooks/auto-format.cjs).
- Logování běhu se ukládá do [frontend/.github/hooks/hook.log](.github/hooks/hook.log).

## 4) Struktura kódu

- `src/pages/*` - obrazovky aplikace (welcome, game, results, leaderboard).
- `src/routing/routes.tsx` - mapování Page -> path + komponenta.
- `src/store/index.ts` - store a root typy.
- `src/store/hooks.ts` - používat useAppDispatch/useAppSelector.
- `src/services/httpClient.ts` - centrální HTTP mutator pro generated klienta.
- `src/services/generated/*` - generated React Query klient (neupravovat ručně).
- `src/services/generated-zod/*` - generated zod schémata (neupravovat ručně).
- `src/languages/csCZ.ts` - české texty UI.
- `src/tests/setup.ts` - test setup pro Vitest.

## 5) Konvence a styl

- UI texty, chyby a lokalizace držet v češtině.
- Při code review popis nalezených problémů psát česky.
- Ignorovat čistě formátovací změny, pokud nemají funkční dopad.
- Při práci se store používat typované hooky ze `src/store/hooks.ts`.
- Při přidání nové routy aktualizovat Page enum, routes mapu a navigaci konzistentně.
- V React komponentách dodržovat oddělení prezentační vrstvy od funkční (aplikační logiky): např. `src/pages/game/index.tsx` (prezentační) a `src/pages/game/useGame.ts` (aplikační logika).
- Jednořádkové JSDoc komentáře psát ve formátu: `/** Jednořádkový komentář */`.

## 6) API a codegen workflow

Orval načítá OpenAPI podle proměnných v .env:

```env
VITE_API_BASE_URL=http://localhost:8080
VITE_API_SWAGGER_JSON_PATH=/v3/api-docs
```

Po změně API specifikace nebo endpointu:

```powershell
npm run api:generate
npm run test:api-contract
```

Před PR preferovat:

```powershell
npm run api:verify
```

## 7) Lint a test guardrails

- V ESLint je `react-hooks/exhaustive-deps` aktuálně vypnuté. Kontroluj dependency pole manuálně.
- Pro generated soubory jsou některá pravidla změkčena (např. explicit any). Nepřenášet to do hand-written kódu.
- Vitest běžně běží v jsdom, setup je v `src/tests/setup.ts`.

## 8) Co neupravovat ručně

- `src/services/generated/**`
- `src/services/generated-zod/**`

Tyto soubory se regenerují příkazem `npm run api:generate`.

## 9) Checklist před PR

1. Spustit lint: `npm run lint`.
2. Spustit testy: `npm run test:coverage`.
3. Pokud se měnilo API nebo kontrakty, spustit: `npm run api:verify`.
4. Zkontrolovat, že nebyly omylem ručně editovány generated soubory mimo codegen workflow.
5. Zkontrolovat, že nové UI texty jsou v češtině a konzistentní s překlady.
