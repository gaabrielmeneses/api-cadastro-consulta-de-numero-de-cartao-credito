# Credit Card API

Secure REST API for credit card registration and consultation with **Java 21**, **Spring Boot 3.2** and **Hexagonal Architecture**.

## 🚀 Technologies

- Java 21, Spring Boot 3.2, Spring Security, JWT
- MySQL 8, JPA/Hibernate, HikariCP
- AES-256-GCM, HMAC-SHA256, BCrypt
- Docker, Swagger, JUnit 5, Testcontainers

## 🔒 Security

- **Encryption**: AES-256-GCM for card numbers
- **Authentication**: JWT with 1-hour expiration
- **Default user**: `admin` / `cont1234`

## 📊 Endpoints

```http
# Login
POST /auth/login
{"username": "admin", "password": "cont1234"}

# Create card
POST /cards
Authorization: Bearer <token>
{"cardNumber": "4532015112830366"}

# Search card
GET /cards/{cardNumber}
Authorization: Bearer <token>

# Batch upload
POST /cards/batch
Authorization: Bearer <token>
Content-Type: multipart/form-data
```

## 🏃♂️ Execution

### Docker (Recommended)
```bash
git clone <repository-url>
cd api-cadastro-consulta-de-numero-de-cartao-credito
docker-compose up --build
```

**Access:**
- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html

### Local
```bash
# 1. MySQL
docker run --name mysql8 \
  -e MYSQL_ROOT_PASSWORD=cont1234 \
  -e MYSQL_DATABASE=creditcard \
  -e MYSQL_USER=admin \
  -e MYSQL_PASSWORD=cont1234 \
  -p 3306:3306 -d mysql:8.0

# 2. Environment variables
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/creditcard?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
export SPRING_DATASOURCE_USERNAME=admin
export SPRING_DATASOURCE_PASSWORD=cont1234
export SECURITY_JWT_SECRET=mySecretKey123456789012345678901234567890
export ENCRYPTION_KEY=myEncryptionKey1234567890123456

# 3. Run
./mvnw spring-boot:run
```

## 🧪 Tests

```bash
./mvnw test                    # All tests
./mvnw clean test jacoco:report # With coverage
```

## 📋 Testing with Postman/Insomnia

1. **Start application and database:**
```bash
docker-compose up --build
```

2. **Import collection:**
   - Postman: Import `collection/Credit-Card-API.postman_collection.json`
   - Insomnia: Import the same file

3. **Test endpoints:**
   - Execute "Login" first to get the token
   - Token will be automatically used in other endpoints
   - Test "Create Card", "Get Card" and "Batch Upload"

## 📋 Usage Example (cURL)

```bash
# Login
TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"cont1234"}' | jq -r '.token')

# Create card
curl -X POST http://localhost:8080/cards \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"cardNumber":"4532015112830366"}'

# Search card
curl -X GET http://localhost:8080/cards/4532015112830366 \
  -H "Authorization: Bearer $TOKEN"
```