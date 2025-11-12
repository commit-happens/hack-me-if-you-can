## Frontend pomocné příkazy
fe-dev:
	echo "Running frontend development server..."
	cd frontend && npm run dev

fe-lint:
	echo "Running frontend linting..."
	cd frontend && npm run lint

fe-build:
	echo "Building frontend..."
	cd frontend && npm run build

fe-preview:
	echo "Running frontend preview server..."
	cd frontend && npm run preview

fe-sync-deps:
	echo "Running frontend preview server..."
	cd frontend && npm ci


## Backend pomocné příkazy
be-clean:
	echo "Running backend cleaning of /target and helping files..."
	cd backend && mvn clean

be-compile:
	echo "Running compiling of main data into /target..."
	cd backend && mvn compile

be-package:
	echo "Running buildling .jar package of main data into /target..."
	cd backend && mvn package -DskipTests

be-test:
	echo "Running tests for backend..."
	cd backend && mvn test

be-run:
	echo "Running compiling of main data into /target..."
	cd backend && mvn spring-boot:run

be-hosts:
	@echo "--- Uživatelské a vývojářské odkazy ---"
	@echo "Hello API: http://localhost:8080/api/hello"
	@echo "H2 database: http://localhost:8080/h2-console (user/pass viz application.properties)"
	@echo "Swagger API documentation: http://localhost:8080/swagger-ui.html"
