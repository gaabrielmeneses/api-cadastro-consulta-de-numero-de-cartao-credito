# Credit Card API

API REST para **cadastro e consulta de números de cartão**, usando **Java 21**, **Spring Boot 3.2** e **Arquitetura Hexagonal**, com foco em **segurança, performance e testabilidade**.

## 🚀 Tecnologias

* Java 21, Spring Boot 3.2, Spring Security + JWT
* MySQL 8, JPA/Hibernate, HikariCP
* AES-256-GCM, HMAC-SHA256, BCrypt
* Docker, Swagger, Testcontainers

## 🔒 Segurança

* Criptografia de cartões via **AES-256-GCM**
* Autenticação **JWT** (expiração: 1h)
* Usuário padrão: `admin` / `admin123`

## 📘 Endpoints Principais

```http
POST /auth/login
POST /cards         # Criar cartão
GET  /cards/{num}   # Consultar cartão
POST /cards/batch   # Upload em lote (multipart/form-data)
```

## 🏃 Execução

### Via Docker (recomendado)

```bash
git clone <repository-url>
cd api-cadastro-consulta-de-numero-de-cartao-credito
docker-compose up --build
```

**Acesso:**

* API: [http://localhost:8080](http://localhost:8080)
* Swagger: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Execução Local

1. **Subir MySQL**

```bash
docker run --name mysql8 \
  -e MYSQL_ROOT_PASSWORD=cont1234 \
  -e MYSQL_DATABASE=creditcard \
  -e MYSQL_USER=admin \
  -e MYSQL_PASSWORD=cont1234 \
  -p 3306:3306 -d mysql:8
```

2. **Variáveis de ambiente**

```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/creditcard
export SPRING_DATASOURCE_USERNAME=admin
export SPRING_DATASOURCE_PASSWORD=cont1234
export SECURITY_JWT_SECRET=mySecretKey123456789012345678901234567890
export ENCRYPTION_KEY=myEncryptionKey1234567890123456
```

3. **Rodar**

```bash
./mvnw spring-boot:run
```

## 🧪 Testes

```bash
./mvnw test
./mvnw clean test jacoco:report
```

## 📁 Upload em Lote (Batch)

Formato posicional esperado:

```
DESAFIO-HYPERATIVA           20180524LOTE0001000010
C1     4456897999999999
C2     4456897922969999
LOTE0001000010
```

Exemplo:

```bash
curl -X POST http://localhost:8080/cards/batch \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@cards.txt"
```

Resposta:

```json
{ "processedCards": 2 }
```

## 📬 Postman / Insomnia

* Suba o ambiente com `docker-compose up --build`
* Importe a collection: `collection/Credit-Card-API.postman_collection.json`
* Execute “Login” e teste os demais endpoints

## 📌 Exemplos (cURL)

```bash
# Login
TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | jq -r '.token')

# Criar cartão
curl -X POST http://localhost:8080/cards \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"cardNumber":"4532015112830366"}'

# Consultar
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/cards/4532015112830366

# Batch
curl -X POST http://localhost:8080/cards/batch \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@cards.txt"
```

