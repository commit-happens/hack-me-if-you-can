# Hack me if you can

**Hack me if you can** je interaktivní webová aplikace zaměřená na rozpoznávání phishingových e-mailů a školení kybernetické bezpečnosti. Hráči analyzují e-mailové zprávy, rozhodují, zda jsou legitimní nebo podvodné, přičemž špatné odpovědi snižují jejich skóre uložené v databázi. Aplikace kombinuje React 19 frontend s real-time hodnocením a Spring Boot backend s REST API pro správu hráčů a jejich výsledků.

## Tech stack

- Frontend: React 19 + TypeScript, ESLint, Vite 7 (dev server, build, preview)
- Backend: Java 21, Spring Boot, H2, Maven

## Struktura repozitáře

```
hack-me-if-you-can/
├─ Makefile                           # pohodlné příkazy (fe-dev, fe-lint, fe-build, fe-preview)
├─ backend/                           # Spring Boot + Maven aplikace
│  ├─ src/
│  │  ├─ main
│  │  │  ├─ java/cz/hackmeifyoucan     # vše napsané v java (controller, entity, repository, service, hlavní backend Applikace)
│  │  │  └─ resources                  # vše ostatní (konfigurace portu, databáze)
│  │  └─ test
│  ...
│  └─ pom.xml                        # základní informace o projektu, závislosti (dependencies), pluginy
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

## 🐳 Rychlý start: Docker (Frontend + Backend najednou)

Nejrychlejší způsob, jak spustit celou aplikaci bez lokální instalace Node.js, Maven nebo JDK.

### Předpoklady

- **Docker Desktop** (obsahuje Docker Engine a Docker Compose)

### Spuštění

```bash
docker compose up --build
```

Po úspěšném buildu a spuštění budou dostupné:

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/hello
- **Swagger API**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console

### Zastavení

```bash
docker compose down
```

Podrobné informace najdeš v [DOCKER.md](DOCKER.md).

---

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

- Soubor: `frontend/.env` (vytvořte zkopírováním z `frontend/.env.example`)
- Pro lokální nastavení zkopírujte `.env.example` na `.env` a upravte hodnoty dle potřeby
- Příklad:

  ```env
  VITE_API_LOCALE=cs-CZ
  VITE_TIME_PER_QUESTION=60
  VITE_GAME_QUESTIONS_LIMIT=20
  ```

Vite načítá proměnné s prefixem `VITE_`. Hodnotu může aplikace použít např. pro výběr překladu nebo nastavení herních pravidel.

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
    lsof -i :5173   # zjistí PID
    kill <PID>      # např. kill 69588
    ```
- Instalace závislostí selže: ověř, že používáš `npm ci` (existuje `package-lock.json`). Pokud je lock neaktuální, smaž `node_modules` a `package-lock.json` a spusť `npm install`.
- Windows + `make` není nalezen: otevři nové okno terminálu po instalaci, případně přidej binární složku `make` do PATH nebo spouštěj targety přes `fe.ps1`.

---

## Rychlý start: backend

- Technologie: Java 21, Spring Boot, Maven
- Hlavní třída: `cz.hackmeifyoucan.backend.HackMeIfYouCanApplication`
- Výchozí port: 8080 (konfigurovatelné v `application.properties`)

### Spuštění (lokálně)

1. Klonování repozitáře:

```bash
git clone https://github.com/commit-happens/hack-me-if-you-can.git
```

2. Spuštění aplikace v režimu vývoje:

```bash
make be-run
```

3. Vytvoření spustitelného jar (production):

```bash
make be-clean
make be-package
java -jar target/*.jar
```

4. Spuštění testů (důležité):

```bash
mvn test
```

### Dostupné odkazy po spuštění backendu

#### REST api

1. Testovací Hello api: http://localhost:8080/hello
2. Players api, vrátí json všech hráčů dostupných v databázi: http://localhost:8080/players

#### Swagger API documentation

URL: http://localhost:8080/swagger-ui.html

#### H2 konzole (in-memory databáze)

URL: http://localhost:8080/h2-console

Projekt používá H2 databázi v paměťovém režimu (in-memory), ideální pro lokální vývoj a testování.

Otevřeš ji v prohlížeči na následující adrese:

Přihlašovací údaje (výchozí) najdeš v backend/src/main/resources/application.propertie

Tipy po přihlášení:

- Prohlížet a upravovat tabulky
- Spouštět vlastní SQL dotazy
- Zkontrolovat uložená data přímo během běhu aplikace

Flyway

```
cd backend && mvn flyway:repair -Dflyway.url=jdbc:postgresql://localhost:5432/hack_db -Dflyway.user=postgres -Dflyway.password=postgres@123
```

---

Ročníkový projekt XRPR1 by Commit Happens
