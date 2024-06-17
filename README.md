# Wallet-Service

- The Wallet service provides a set of API to manage wallet-related operations for players. This includes
  authentication, balance inquiries, debit and credit transactions, and retrieving transaction history.
- Application can be executed in 2 profiles: `dev` & `test`.
- Both profiles uses an in-memory H2 database.
- In `test` profile data is persisted across restarts, but not in `dev`.

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

# NOTE

- In test profile, data will be stored at path `/h2/wallet`. So for first time you might need to start application
  with sudo to create folder, if application isn't having permission.
