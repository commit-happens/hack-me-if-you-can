# Načtení proměnných z .env souboru
ifneq (,$(wildcard .env))
    include .env
    export
endif


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

be-flyway-repair:
	@echo "Oprava Flyway migrací backendu..."
	cd backend && mvn flyway:repair -Dflyway.url=jdbc:postgresql://localhost:5432/$(DB_NAME) -Dflyway.user=$(DB_USER) -Dflyway.password=$(DB_PASSWORD)


## Databázové pomocné příkazy
db-connect:
	@echo "Připojení k PostgreSQL databázi $(DB_NAME)..."
	PGPASSWORD=$(DB_PASSWORD) psql -U $(DB_USER) -d $(DB_NAME) -h localhost

db-tables:
	@echo "Seznam tabulek v databázi $(DB_NAME):"
	@PGPASSWORD=$(DB_PASSWORD) psql -U $(DB_USER) -d $(DB_NAME) -h localhost -c "\dt"

db-players:
	@echo "Obsah tabulky players:"
	@PGPASSWORD=$(DB_PASSWORD) psql -U $(DB_USER) -d $(DB_NAME) -h localhost -c "SELECT * FROM players ORDER BY id;"

db-players-count:
	@echo "Počet hráčů v databázi:"
	@PGPASSWORD=$(DB_PASSWORD) psql -U $(DB_USER) -d $(DB_NAME) -h localhost -c "SELECT COUNT(*) FROM players;"

db-flyway-status:
	@echo "Stav Flyway migrací:"
	@PGPASSWORD=$(DB_PASSWORD) psql -U $(DB_USER) -d $(DB_NAME) -h localhost -c "SELECT version, description, success, installed_on FROM flyway_schema_history ORDER BY installed_rank;"

db-reset:
	@echo "Reset databáze (smazání tabulek a Flyway historie)..."
	PGPASSWORD=$(DB_PASSWORD) psql -U $(DB_USER) -d $(DB_NAME) -h localhost -c "DROP TABLE IF EXISTS flyway_schema_history, players CASCADE;"
	@echo "Databáze resetována. Spusť 'make be-run' pro novou migraci."


## Další pomocné příkazy
print-hosts:
	@echo "--- Uživatelské a vývojářské odkazy ---"
	@echo "Frontend (local) server: http://localhost:5173"
	@echo "Frontend (docker) preview: http://localhost:3000"
	@echo "Hello API: http://localhost:8080/api/hello"
	@echo "Players API: http://localhost:8080/api/players"
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

docker-db-connect:
	@echo "Připojení k PostgreSQL v Dockeru..."
	docker exec -it hmiyc-postgres psql -U $(DB_USER) -d $(DB_NAME)

docker-db-tables:
	@echo "Seznam tabulek v Docker databázi:"
	@docker exec -it hmiyc-postgres psql -U $(DB_USER) -d $(DB_NAME) -c "\dt"

docker-db-players:
	@echo "Obsah tabulky players v Docker databázi:"
	@docker exec -it hmiyc-postgres psql -U $(DB_USER) -d $(DB_NAME) -c "SELECT * FROM players ORDER BY id;"
