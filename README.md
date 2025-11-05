# Hack me if you can

Jednoduchý monorepo základ pro frontend (React + Vite + TypeScript). Tento dokument popisuje strukturu projektu a jak lokálně spustit frontend.

## Tech stack

- React 19 + TypeScript
- Vite 7 (dev server, build, preview)
- ESLint

## Struktura repozitáře

```
hack-me-if-you-can/
├─ Makefile               # pohodlné příkazy (fe-dev, fe-lint, fe-build, fe-preview)
├─ fe.ps1                 # PowerShell alternativa k Makefile (Windows)
├─ frontend/              # React + Vite aplikace
│  ├─ index.html
│  ├─ package.json        # skripty: dev, build, preview, lint
│  ├─ vite.config.ts
│  ├─ src/
│  └─ .env                # lokální proměnné (např. VITE_API_LOCALE)
└─ ...
```

## Předpoklady

- Node.js 18+ (LTS doporučeno)
- npm (součást Node.js)
- Volitelné: GNU Make pro spouštění targetů z `Makefile`

## Rychlý start: frontend

Z kořene repozitáře (Windows PowerShell):

```powershell
cd frontend
npm ci
npm run dev
```

Poté otevři: http://localhost:5173/

Zastavení serveru: Ctrl+C.

### Alternativy spouštění

- Pomocí `Makefile` (z kořene repa):

  ```powershell
  make fe-dev
  make fe-lint
  make fe-build
  make fe-preview
  ```

  Pozn.: Na Windows je potřeba mít nainstalovaný GNU Make (např. winget/choco/scoop) a/nebo spouštět make v Git Bash/MSYS2/WSL.

## Environment proměnné (frontend)

- Soubor: `frontend/.env`
- Příklad:

  ```env
  VITE_API_LOCALE=csCZ
  ```

Vite načítá proměnné s prefixem `VITE_`. Hodnotu může aplikace použít např. pro výběr překladu.

## Build a náhled produkce (frontend)

```powershell
cd frontend
npm run build     # vytvoří produkční build do dist/
npm run preview   # spustí lokální server nad dist/
```

## Lint

```powershell
cd frontend
npm run lint
```

## Troubleshooting

- Port 5173 je obsazený: 
    - Spusť frontend na jiném portu:
        ```powershell
        npm run dev -- --port 5174
        ```
    - Nebo zjisti PID procesu, který port používá, a ukonči ho:
        ```powershell
        lsof -i :5173   # zjistí PID
        kill <PID>      # např. kill 69588
        ```
- Instalace závislostí selže: ověř, že používáš `npm ci` (existuje `package-lock.json`). Pokud je lock neaktuální, smaž `node_modules` a `package-lock.json` a spusť `npm install`.
- Windows + `make` není nalezen: otevři nové okno terminálu po instalaci, případně přidej binární složku `make` do PATH nebo spouštěj targety přes `fe.ps1`.

---

Má-li se projekt rozšířit o backend, přidej do kořene další sekci s instrukcemi pro `backend/` a případné specifické `.gitignore` do jednotlivých balíků.

Ročníkový projekt XRPR1 by Commit Happens