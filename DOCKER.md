# 🐳 Docker Compose - Spuštění aplikace

Tento dokument popisuje, jak spustit celou aplikaci (backend + frontend) pomocí Docker Compose bez nutnosti lokální instalace Maven, npm nebo JDK.

## Předpoklady

Na tvém počítači musí být nainstalovaný pouze:
- **Docker Desktop** (obsahuje Docker Engine a Docker Compose)
  - macOS: [https://docs.docker.com/desktop/install/mac-install/](https://docs.docker.com/desktop/install/mac-install/)

## Spuštění aplikace

### 1. Spuštění všech služeb

```bash
docker-compose up --build
```

Tento příkaz:
- Stáhne potřebné Docker image (Maven, JDK, Node.js, Nginx)
- Nabuiluje backend (Java 21 + Spring Boot)
- Nabuiluje frontend (React + Vite)
- Spustí obě služby

### 2. Přístup k aplikaci

Po úspěšném spuštění budou dostupné:

- **Frontend**: [http://localhost:3000](http://localhost:3000)
- **Backend API**: [http://localhost:8080/api/hello](http://localhost:8080/api/hello)
- **H2 Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - JDBC URL: `jdbc:h2:file:./data/hack_db`
  - Username: `sa`
  - Password: (prázdné)
- **Swagger API**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### 3. Zastavení aplikace

```bash
# Graceful stop
docker-compose down

# Stop a smazání volume (databáze)
docker-compose down -v

# Stop a smazání images
docker-compose down --rmi all
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

Pro produkční nasazení doporuču:
- Použít externí databázi (PostgreSQL, MySQL) místo H2 in-memory
- Přidat volume pro perzistenci dat
- Nastavit environment variables přes `.env` soubor
- Použít `docker-compose.prod.yml` pro produkční konfiguraci
- Přidat health checks a restart policies
