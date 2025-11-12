# Hack me if you can

Jednoduchý monorepo základ pro frontend (React + Vite + TypeScript). Tento dokument popisuje strukturu projektu a jak lokálně spustit frontend.

## Tech stack
- Frontend: React 19 + TypeScript, ESLint, Vite 7 (dev server, build, preview)
- Backend: Java 21, Spring Boot, H2, Maven, (Lombok)


## Struktura repozitáře

```
hack-me-if-you-can/
├─ Makefile                           # pohodlné příkazy (fe-dev, fe-lint, fe-build, fe-preview)
├─ backend/                           # Springboot + Maven aplikace
│  ├─ src/
│  │  ├─ main
│  │  │  ├─ java.cz.hackmeifyoucan     # vše napsané v java (controller, entity, repository, service, hlavní backend Applikace)
│  │  │  └─ resources                  # vše ostatní (konfigurace portu, databáze)
│  │  └─ test
│  ...
│  └─ .pom.xml                        # základní informace o projektu, závislosti (dependencies), pluginy
├─ frontend/                          # React + Vite aplikace
│  ├─ src/
│  ├─ index.html
│  ├─ package.json                    # skripty: dev, build, preview, lint
│  ├─ vite.config.ts
│  ...
│  └─ .env                            # lokální proměnné (např. VITE_API_LOCALE)
└─ ...
```

## Předpoklady
- Frontend
  - Node.js 18+ (LTS doporučeno)
  - npm (součást Node.js)
  - Volitelné: GNU Make pro spouštění targetů z `Makefile`
- Backend
  - Java 21 (LTS)
  - Maven 3.9+
  - Doporučené IDE: IntelliJ IDEA (doporučeno) nebo VS Code
  - Doporučený Lombok plugin v IDE

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
  make fe-sync-deps
  make fe-dev
  ```

  Pozn.: Na Windows je potřeba mít nainstalovaný GNU Make (např. winget/choco/scoop) a/nebo spouštět make v Git Bash/MSYS2/WSL.

### Environment proměnné (frontend)

- Soubor: `frontend/.env`
- Příklad:

  ```env
  VITE_API_LOCALE=csCZ
  ```

Vite načítá proměnné s prefixem `VITE_`. Hodnotu může aplikace použít např. pro výběr překladu.

### Build a náhled produkce (frontend)

```powershell
make fe-build     # vytvoří produkční build do dist/
make fe-preview   # spustí lokální server nad dist/
```

### Lint

```powershell
make fe-lint
```

### Troubleshooting

- Port 5173 je obsazený: 
    - Spusť frontend na jiném portu:
        ```powershell
        make fe-dev -- --port 5174
        ```
    - Nebo zjisti PID procesu, který port používá, a ukonči ho:
        ```powershell
        lsof -i :5174   # zjistí PID
        kill <PID>      # např. kill 69588
        ```
- Instalace závislostí selže: ověř, že používáš `npm ci` (existuje `package-lock.json`). Pokud je lock neaktuální, smaž `node_modules` a `package-lock.json` a spusť `npm install`.
- Windows + `make` není nalezen: otevři nové okno terminálu po instalaci, případně přidej binární složku `make` do PATH nebo spouštěj targety přes `fe.ps1`.

---

## Rychlý start: backend
- Technologie: Java 21, Spring Boot, Maven, (Lombok)
- Hlavní třída: `cz.hackmeifyoucan.backend.HackMeIfYouCanApplication`
- Výchozí port: 8080 (konfigurovatelné v `application.properties`)

### Spuštění (lokálně)
1) Klon repozitář:

```bash
git clone https://github.com/commit-happens/hack-me-if-you-can.git
```

2) Spuštění aplikace v režimu vývoje:

```bash
make be-run
```

3) Vytvoření spustitelného jar (production):

```bash
make be-clean
make be-package
java -jar target/*.jar
```

4) Spuštění testů (důležité):

```bash
mvn test
```

### Dostupné odkazy po spuštění backendu
#### REST api
1. Testovací Hello api: http://localhost:8080/api/hello"
2. Players api, vrátí json všech hráčů dostupných v databázi: http://localhost:8080/api/players"

#### Swagger API documentation
URL: http://localhost:8080/swagger-ui.html"

#### H2 konzole (in-memory databáze)
URL: http://localhost:8080/h2-console

Projekt používá H2 databázi v paměťovém režimu (in-memory), ideální pro lokální vývoj a testování.

Otevřeš ji v prohlížeči na následující adrese:


Přihlašovací údaje (výchozí) najdeš v backend/src/main/resources/application.propertie

Tipy po přihlášení:

- Prohlížet a upravovat tabulky
- Spouštět vlastní SQL dotazy
- Zkontrolovat uložená data přímo během běhu aplikace


-----

Ročníkový projekt XRPR1 by Commit Happens