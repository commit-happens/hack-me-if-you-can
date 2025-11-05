Ročníkový projekt XRPR1 by Commit Happens

## Springboot
Requirements: Java+17, Spring Boot Extension Pack in VSCode, (Optional) Maven (Pro Maven Wrapper neni treba instalace)

Installation:
1. Windows via Chocolatey
    1. First, install Chocolatey if not already installed
        ```choco install openjdk21 ```
    2. Verify
        ```java -version```
2. Mac via Homebrew
    ```brew install openjdk@21 maven```
    1. You may need to add Java to PATH:
    ```export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"```
    ```export JAVA_HOME=$(/usr/libexec/java_home -v21)```
    ```export PATH=$JAVA_HOME/bin:$PATH```
    ```source ~/.zshrc```
    2. Verify
    ```maven --version```
    ```java --version```


### Guides
The following guides illustrate how to use some features concretely:
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)


### Maven Wrapper
* Clean the compiled /target dir
```./mvnw clean```
* Compile the package in /target dir (includes complie, test, package mvnw commands)
```./mvnw clean verify```
* Need to follow the Standard Directory Layout for Maven project: https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html


## Backend Project Structure

```
src/
├── main/
│   ├── java/cz/hackmeifyoucan/backend/
│   │   ├── config/         # Configuration classes
│   │   ├── controller/     # REST controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # JPA entities
│   │   ├── repository/     # Data repositories
│   │   └── service/        # Business logic
│   └── resources/
│       └── application.properties
└── test/
    ├── java/
    └── resources/
        └── application-test.properties
```

## FE Commands
Run the backend
```
mvn spring-boot:run
```
or (without installed mvn)
```
/.mvnw spring-boot:run
```
You can then checkout:
1. Swagger UI with APIs: http://localhost:8085/swagger-ui/index.html
2. Check that BE is running: http://localhost:8085/players