# 🐳 Docker Compose - Spuštění aplikace

Tento dokument popisuje, jak spustit celou aplikaci (backend + frontend + PostgreSQL) pomocí Docker Compose bez nutnosti lokální instalace Maven, npm nebo JDK.

## Předpoklady

Na tvém počítači musí být nainstalovaný pouze:
- **Docker Desktop** (obsahuje Docker Engine a Docker Compose)
  - macOS: [https://docs.docker.com/desktop/install/mac-install/](https://docs.docker.com/desktop/install/mac-install/)

## Konfigurace prostředí

Před spuštěním vytvoř soubor `.env` v kořenovém adresáři projektu:

```bash
# Databázové přihlašovací údaje
DB_NAME=hmiyc
DB_USER=postgres
DB_PASSWORD=postgres@123
```

## Spuštění aplikace

### 1. Spuštění všech služeb

```bash
docker-compose up --build
```

Tento příkaz:
- Spustí PostgreSQL databázi
- Vytvoří backend pomocí build (Java 21 + Spring Boot)
- Spustí Flyway migrace pro inicializaci databáze
- Vytvoří frontend pomocí build (React + Vite)
- Spustí všechny služby

### 2. Přístup k aplikaci

Po úspěšném spuštění budou dostupné:

| Služba | URL |
|--------|-----|
| **Frontend** | [http://localhost:3000](http://localhost:3000) |
| **Backend API** | [http://localhost:8080/api/hello](http://localhost:8080/api/hello) |
| **Players API** | [http://localhost:8080/api/players](http://localhost:8080/api/players) |
| **Swagger API** | [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) |
| **PostgreSQL** | `localhost:5432` |

### 3. Zastavení aplikace

```bash
# Graceful stop
docker-compose down

# Stop a smazání volumes (databáze)
docker-compose down -v

# Stop a smazání images
docker-compose down --rmi all
```

## PostgreSQL a Flyway

### Architektura databáze

Aplikace používá **PostgreSQL** jako databázi a **Flyway** pro správu migrací:

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│    Frontend     │────▶│    Backend      │────▶│   PostgreSQL    │
│   (Nginx:3000)  │     │ (Spring:8080)   │     │    (:5432)      │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                               │
                               ▼
                        ┌─────────────────┐
                        │     Flyway      │
                        │   (migrace)     │
                        └─────────────────┘
```

### Flyway migrace

Migrace se nacházejí v `backend/src/main/resources/db/migration/`:

| Migrace | Popis |
|---------|-------|
| `V1__create_players_table.sql` | Vytvoření tabulky `players` |
| `V2__insert_initial_players.sql` | Vložení testovacích dat |

Flyway se automaticky spustí při startu backendu a aplikuje všechny pending migrace.

## Docker příkazy pro PostgreSQL

### Připojení k databázi v kontejneru

```bash
# Připojení přes psql
docker exec -it hmiyc-postgres psql -U postgres -d hmiyc

# Spuštění SQL příkazu
docker exec -it hmiyc-postgres psql -U postgres -d hmiyc -c "SELECT * FROM players LIMIT 5;"
```

### Zobrazení tabulek

```bash
docker exec -it hmiyc-postgres psql -U postgres -d hmiyc -c "\dt"
```

### Zobrazení hráčů

```bash
docker exec -it hmiyc-postgres psql -U postgres -d hmiyc -c "SELECT * FROM players ORDER BY id;"
```

### Stav Flyway migrací

```bash
docker exec -it hmiyc-postgres psql -U postgres -d hmiyc -c "SELECT version, description, success FROM flyway_schema_history;"
```

### Reset databáze

```bash
# Smazání volumes (kompletní reset)
docker-compose down -v
docker-compose up --build
```

## Užitečné příkazy

### Spuštění na pozadí (detached mode)
```bash
docker-compose up -d
```

### Zobrazení logů
```bash
# Všechny služby
docker-compose logs -f

# Pouze backend
docker-compose logs -f backend

# Pouze frontend
docker-compose logs -f frontend
```

### Restart služby
```bash
# Restart všech služeb
docker-compose restart

# Restart pouze backendu
docker-compose restart backend
```

### Rebuild konkrétní služby
```bash
# Rebuild pouze backendu
docker-compose build backend

# Rebuild a restart backendu
docker-compose up -d --build backend
```

### Zobrazení běžících kontejnerů
```bash
docker-compose ps
```

### Přístup do kontejneru (debugging)
```bash
# Backend
docker exec -it hmiyc-backend sh

# Frontend
docker exec -it hmiyc-frontend sh
```

## Struktura Docker souborů

```
.
├── docker-compose.yml          # Orchestrace služeb
├── backend/
│   ├── Dockerfile             # Multi-stage build pro Java aplikaci
│   └── .dockerignore          # Ignorované soubory při buildu
└── frontend/
    ├── Dockerfile             # Multi-stage build pro React aplikaci
    ├── nginx.conf             # Konfigurace Nginx serveru
    └── .dockerignore          # Ignorované soubory při buildu
```

## Jak to funguje

### Backend (Multi-stage build)
1. **Stage 1 (build)**: Použije Maven a JDK 21 k buildování `.jar` souboru
2. **Stage 2 (runtime)**: Zkopíruje pouze `.jar` do minimálního JRE image
3. **Výsledek**: Malý produkční image (~200 MB) bez build nástrojů

### Frontend (Multi-stage build)
1. **Stage 1 (build)**: Použije Node.js k buildu React aplikace
2. **Stage 2 (runtime)**: Servíruje statické soubory přes Nginx
3. **Výsledek**: Velmi malý image (~25 MB) s vysokým výkonem

### Nginx proxy
Frontend Nginx automaticky proxy všechny `/api/*` requesty na backend, takže není nutné řešit CORS.

## Troubleshooting

### Port již používán
```bash
# Změň porty v docker-compose.yml
ports:
  - "8081:8080"  # Místo 8080:8080
```

### Nedostatek paměti při buildu
```bash
# Zvyš memory limit v Docker Desktop > Settings > Resources
```

### Backend nenaběhne
```bash
# Zkontroluj logy
docker-compose logs backend

# Restart s rebuildem
docker-compose up -d --build backend
```

### Frontend nenaběhne
```bash
# Zkontroluj logy
docker-compose logs frontend

# Restart s rebuildem
docker-compose up -d --build frontend
```

## Produkční poznámky

- Aplikace používá PostgreSQL jako produkční databázi
- Data jsou perzistentní díky Docker volumes (`postgres-data`)
- Environment variables jsou nastaveny přes `.env` soubor
- Flyway zajišťuje konzistentní stav databázového schématu
