# Wallet-Service

- Wallet service to manage a player's transactions.

- Application can be executed in 2 profiles i.e. `dev` & `test`.
- Both profiles uses in-memory h2 database.
- In `test` profile data is persisted across restarts, but not in `dev`.

# Running service locally

### From IDE

- Import the project in any IDE and create a run configuration with main
  class `com.kulsin.wallet.WalletServiceApplication`
- To run in `test` profile add VM options `-Dspring.profiles.active=dev`

### From Terminal

- Go to `wallet-service` root folder.
- Build the project `mvn clean install`
- Start application `sudo java -jar application/target/application-0.0.1-SNAPSHOT.jar`

# Important Links

H2 DB Console: http://localhost:8080/h2-ui/

Wallet Swagger: http://localhost:8080/swagger-ui/index.html

# NOTE

- In test profile, data will be stored at path `/h2/wallet`. So for first time you might need to start application
  with sudo to create folder, if application isn't having permission.

- First transaction should be a credit, so that player account gets created in db.

# Requests

**Authenticate:**

```bash
curl -u test:test \
     -H "Content-Type: application/json" \
     -d '{
            "playerId":123,
            "sessionToken":"test-token"
          }' \
     -X POST \
     http://localhost:8080/api/wallet/authenticate \
     -v
```

**Credit:**

```bash
curl -u test:test \
    -H "Content-Type: application/json" \
    -d '{
          "playerId": 123,
          "amount": 5.00,
          "type": "credit",
          "currency": "EUR",
          "sessionToken":"test-token",
          "transactionId": 989898
        }' \
    -X POST \
    http://localhost:8080/api/wallet/credit \
    -v
```

**Debit:**

```bash
curl -u test:test \
     -H "Content-Type: application/json" \
     -d '{
           "playerId": 123,
           "amount": 1.5,
           "type": "debit",
           "sessionToken":"test-token",
           "transactionId": 65646456
         }' \
     -X POST \
     http://localhost:8080/api/wallet/debit \
     -v
```

**Get Balance:**

```bash
curl -u test:test \
     -H "Content-Type: application/json" \
     -d '{
          "playerId": 123,
          "sessionToken": "test-token"
          }' \
     -X POST \
     http://localhost:8080/api/wallet/balance \
     -v
```

**Get History:**

```bash
curl -u test:test \
    -H "Content-Type: application/json" \
    -X GET \
    http://localhost:8080/api/wallet/history?playerId=123 \
    -v
```
