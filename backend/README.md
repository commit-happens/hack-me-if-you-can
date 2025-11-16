# 🧠 HMIYC – Backend (Spring Boot)

Tento modul obsahuje backendovou část projektu **Hack Me If You Can**, postavenou na frameworku **Spring Boot**.

---

## Stručně
- Technologie: Java 21, Spring Boot, Maven, (Lombok)
- Hlavní třída: `cz.hackmeifyoucan.backend.HackMeIfYouCanApplication`
- Výchozí port: 8080 (konfigurovatelné v `application.properties`)

---

## Požadavky
- Java 21 (LTS)
- Maven 3.9+
- Doporučené IDE: IntelliJ IDEA (doporučeno) nebo VS Code
- Doporučený Lombok plugin v IDE

---

## Rychlé spuštění (lokálně)
1) Klon repozitář a přejdi do backend složky:

```bash
git clone https://github.com/commit-happens/hack-me-if-you-can.git
cd hack-me-if-you-can/backend
```

2) Spuštění aplikace v režimu vývoje:

```bash
mvn spring-boot:run
```

3) Vytvoření spustitelného jar (production):

```bash
mvn clean package -DskipTests
java -jar target/*.jar
```

4) Spuštění testů (důležité):

```bash
mvn test
```


5) H2 konzole

## H2 konzole (in-memory databáze)

Projekt používá H2 databázi v paměťovém režimu (in-memory), ideální pro lokální vývoj a testování.

Otevřeš ji v prohlížeči na následující adrese:

🔗 URL: http://localhost:8080/h2-console

Přihlašovací údaje (výchozí):

- JDBC URL: `jdbc:h2:file:./data/hack_db`
- User Name: `sa`
- Password: (ponech prázdné)

Tipy po přihlášení:

- Prohlížet a upravovat tabulky
- Spouštět vlastní SQL dotazy
- Zkontrolovat uložená data přímo během běhu aplikace
