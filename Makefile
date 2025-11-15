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

be-hosts:
	@echo "--- Uživatelské a vývojářské odkazy ---"
	@echo "Hello API: http://localhost:8080/api/hello"
	@echo "H2 database: http://localhost:8080/h2-console (user/pass viz application.properties)"
	@echo "Swagger API documentation: http://localhost:8080/swagger-ui.html"
