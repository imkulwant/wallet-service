# Wallet-Service

- Wallet service to manage a player's transactions.

- Application uses in-memory h2 database and data is persisted across restarts.

# Running service locally

### From IDE

- Import the project in any IDE and create a run configuration with main class `com.kulsin.WalletServiceApplication`

### From Terminal
- Go to `wallet-service` root folder.
- Build the project `mvn clean install`
- Start application `sudo java -jar application/target/application-0.0.1-SNAPSHOT.jar`

Data will be stored at path `/h2/wallet` so for first time you might need to start application with sudo, so that folder gets created.

For testing you may use postman collection present in application resources or swagger.

# Important Links

H2 DB Console: http://localhost:8080/h2-ui/

Wallet Swagger: http://localhost:8080/swagger-ui/index.html

