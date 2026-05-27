# Načtení proměnných z .env souboru
ifneq (,$(wildcard .env))
    include .env
    export
endif

define PSQL_CMD
PGPASSWORD="$${PGPASSWORD:-$${DB_PASSWORD:-postgres@123}}" psql -h "$${PGHOST:-localhost}" -p "$${PGPORT:-5432}" -U "$${PGUSER:-$${DB_USER:-postgres}}" -d "$${PGDATABASE:-$${DB_NAME:-hmiyc}}"
endef


## Frontend pomocné příkazy
fe-dev:
	@echo "Spuštění vývojářského serveru frontendu..."
	cd frontend && npm run dev

fe-lint:
	@echo "Spuštění lintingu frontendu..."
	cd frontend && npm run lint

fe-build:
	@echo "Buildování frontendu..."
	cd frontend && npm run build

fe-preview:
	@echo "Spuštění preview serveru frontendu..."
	cd frontend && npm run preview

fe-sync-deps:
	@echo "Synchronizace závislostí frontendu..."
	cd frontend && npm ci

fe-test:
	@echo "Spuštění testů frontendu s coverage..."
	cd frontend && npm run test:coverage


## Backend pomocné příkazy
be-clean:
	@echo "Mazání /target a pomocných souborů backendu..."
	cd backend && mvn clean

be-compile:
	@echo "Kompilace hlavních dat do /target..."
	cd backend && mvn compile

be-package:
	@echo "Vytváření .jar balíčku do /target..."
	cd backend && mvn package -DskipTests

be-test:
	@echo "Spuštění testů backendu..."
	cd backend && mvn test

be-run:
	@echo "Spuštění backendu..."
	cd backend && mvn spring-boot:run

## Databázové pomocné příkazy
db-connect:
	@echo "Připojení k PostgreSQL databázi..."
	@$(PSQL_CMD)

db-tables:
	@echo "Seznam tabulek v databázi..."
	@$(PSQL_CMD) -c "\dt"

db-flyway-status:
	@echo "Stav Flyway migrací..."
	@$(PSQL_CMD) -c "SELECT version, description, success, installed_on FROM flyway_schema_history ORDER BY installed_rank;"

db-flyway-repair:
	@echo "Oprava Flyway migrací backendu..."
	cd backend && mvn flyway:repair \
		-Dflyway.url=jdbc:postgresql://$${PGHOST:-localhost}:$${PGPORT:-5432}/$${PGDATABASE:-$${DB_NAME:-hmiyc}} \
		-Dflyway.user=$${PGUSER:-$${DB_USER:-postgres}} \
		-Dflyway.password=$${PGPASSWORD:-$${DB_PASSWORD:-postgres@123}}

db-flyway-clean:
	@echo -n "⚠️ VAROVÁNÍ: Opravdu chcete smazat VŠECHNY tabulky a data z databáze? [y/N]: " && \
	read ans && [ "$$ans" = "y" ] || [ "$$ans" = "Y" ] || (echo "Operace zrušena."; exit 1)
	@echo "Probíhá čištění databáze..."
	cd backend && mvn flyway:clean \
		-Dflyway.cleanDisabled=false \
		-Dflyway.url=jdbc:postgresql://$${PGHOST:-localhost}:$${PGPORT:-5432}/$${PGDATABASE:-$${DB_NAME:-hmiyc}} \
		-Dflyway.user=$${PGUSER:-$${DB_USER:-postgres}} \
		-Dflyway.password=$${PGPASSWORD:-$${DB_PASSWORD:-postgres@123}}


## Další pomocné příkazy
print-hosts:
	@echo "--- Uživatelské a vývojářské odkazy ---"
	@echo "Frontend (local) server: http://localhost:5173"
	@echo "Frontend (docker) preview: http://localhost:3000"
	@echo "Hello API: http://localhost:8080/hello"
	@echo "Players API: http://localhost:8080/players"
	@echo "Swagger API documentation: http://localhost:8080/swagger-ui.html"


## Docker pomocné příkazy
docker-build:
	@echo "Buildování Docker obrazů..."
	docker compose build --no-cache

docker-up-detached:
	@echo "Spuštění všech služeb v Dockeru na pozadí..."
	docker compose up -d --build

docker-down:
	@echo "Zastavení všech služeb..."
	docker compose down

docker-down-volumes:
	@echo "Zastavení všech služeb a smazání volumes (reset databáze)..."
	docker compose down -v

docker-restart:
	@echo "Restart všech služeb..."
	docker compose restart
