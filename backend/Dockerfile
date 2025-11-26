# ==================================
# 🏗️ STAGE 1: Build Stage
# Kompilace a buildování aplikace
# ==================================
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Kopírování Maven wrapper a pom.xml pro cachování závislostí
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Nastavení execute práv pro mvnw
RUN chmod +x ./mvnw

# Stažení závislostí (tato vrstva bude cachována pokud se pom.xml nezmění)
RUN ./mvnw dependency:go-offline

# Kopírování zdrojového kódu
COPY src ./src

# Build aplikace (v Docker buildu přeskočíme testy, aby se nevyžadovala běžící DB)
RUN ./mvnw clean package -DskipTests

# ==================================
# 🚀 STAGE 2: Runtime Stage
# Spuštění aplikace v minimálním prostředí
# ==================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Kopírování pouze JAR souboru z build stage
COPY --from=build /app/target/*.jar app.jar

# Exponování portu pro Spring Boot aplikaci
EXPOSE 8080

# Spuštění aplikace
ENTRYPOINT ["java", "-jar", "app.jar"]
