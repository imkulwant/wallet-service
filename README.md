# Wallet-Service

- Wallet service to manage a player's transactions.

- Application can be executed in 2 profiles i.e. `default` & `test`.
- Both profiles uses in-memory h2 database.
- In `default` profile data is persisted across restarts, but not in `test`.

# Running service locally

### From IDE

- Import the project in any IDE and create a run configuration with main class `com.kulsin.WalletServiceApplication`
- To run in `test` profile add VM options `-Dspring.profiles.active=test`

### From Terminal
- Go to `wallet-service` root folder.
- Build the project `mvn clean install`
- Start application `sudo java -jar application/target/application-0.0.1-SNAPSHOT.jar`

# Important Links

H2 DB Console: http://localhost:8080/h2-ui/

Wallet Swagger: http://localhost:8080/swagger-ui/index.html


# NOTE
- In default profile, data will be stored at path `/h2/wallet`. So for first time you might need to start application with sudo to create folder, if application isn't having permission.

- For testing you may use postman collection present in application resources or swagger.

- First transaction should be a credit, so that player account gets created in db.

