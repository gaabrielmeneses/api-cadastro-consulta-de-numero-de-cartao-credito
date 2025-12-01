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

## 🏃 Execução via Docker

```bash
git clone <repository-url>
cd api-cadastro-consulta-de-numero-de-cartao-credito
docker-compose up --build
```

**Acesso:**

* API: [http://localhost:8080](http://localhost:8080)
* Swagger: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

**Aguarde alguns segundos** para que o MySQL inicialize completamente antes de testar os endpoints.

## 🧪 Testes

```bash
./mvnw test
./mvnw clean test jacoco:report
```

## 📁 Upload em Lote (Batch)

Exemplo:

```bash
curl -X POST http://localhost:8080/cards/batch \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@cards.txt"
```

Resposta:

```json
{ "processedCards": 0 }
```

## 📬 Testando com Postman

### 1. Preparação

* Suba o ambiente: `docker-compose up --build`
* Importe a collection: `collection/Credit-Card-API.postman_collection.json`
* Aguarde a aplicação estar disponível em [http://localhost:8080](http://localhost:8080)

### 2. Fluxo de Teste

**Passo 1: Fazer Login**
* Execute o request "Login" na collection
* O token será automaticamente capturado e configurado para os demais requests

**Passo 2: Testar Endpoints**
* Execute "Criar Cartão" para cadastrar um novo cartão
* Execute "Consultar Cartão" para buscar o cartão criado
* Execute "Upload em Lote" para testar o upload de arquivo

**Observação:** A collection já está configurada com scripts que capturam o token automaticamente após o login e o aplicam nos headers dos demais requests.

### 3. Dicas Importantes

* **Token é gerenciado automaticamente** pela collection - não precisa copiar manualmente
* **Token expira em 1 hora** - refaça o login se necessário
* **Números de cartão são criptografados** no banco de dados
* **Use o Swagger** ([http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)) para documentação interativa
* **Verifique logs** com `docker-compose logs app` em caso de erro

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