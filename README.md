# Credit Card API

API REST segura para cadastro e consulta de números de cartão de crédito com **Java 21**, **Spring Boot 3.2** e **Arquitetura Hexagonal**.

## 🚀 Tecnologias

- Java 21, Spring Boot 3.2, Spring Security, JWT
- MySQL 8, JPA/Hibernate, HikariCP
- AES-256-GCM, HMAC-SHA256, BCrypt
- Docker, Swagger, JUnit 5, Testcontainers

## 🔒 Segurança

- **Criptografia**: AES-256-GCM para números de cartão
- **Autenticação**: JWT com expiração de 1 hora
- **Usuário padrão**: `admin` / `admin123`

## 📊 Endpoints

```http
# Login
POST /auth/login
{"username": "admin", "password": "admin123"}

# Criar cartão
POST /cards
Authorization: Bearer <token>
{"cardNumber": "4532015112830366"}

# Buscar cartão
GET /cards/{cardNumber}
Authorization: Bearer <token>

# Upload em lote
POST /cards/batch
Authorization: Bearer <token>
Content-Type: multipart/form-data
```

## 🏃♂️ Execução

### Docker (Recomendado)
```bash
git clone <repository-url>
cd api-cadastro-consulta-de-numero-de-cartao-credito
docker-compose up --build
```

**Acesso:**
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

# 2. Variáveis de ambiente
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/creditcard?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
export SPRING_DATASOURCE_USERNAME=admin
export SPRING_DATASOURCE_PASSWORD=cont1234
export SECURITY_JWT_SECRET=mySecretKey123456789012345678901234567890
export ENCRYPTION_KEY=myEncryptionKey1234567890123456

# 3. Executar
./mvnw spring-boot:run
```

## 🧪 Testes

```bash
./mvnw test                    # Todos os testes
./mvnw clean test jacoco:report # Com cobertura
```

## 📁 API Batch - Upload em Lote

A API permite o cadastro de múltiplos cartões via upload de arquivo de texto.

### Formato do Arquivo

O arquivo deve seguir o formato posicional:

```
DESAFIO-HYPERATIVA           20180524LOTE0001000010   // Cabeçalho: [01-29]NOME [30-37]DATA [38-45]LOTE [46-51]QTD
C1     4456897999999999                               // Registro: [01-01]ID [02-07]NUM_LOTE [08-26]NUMERO_CARTAO
C2     4456897922969999
C3     4456897999999999
LOTE0001000010                                        // Rodapé: [01-08]LOTE [09-14]QTD
```

### Exemplo de Uso

```bash
# Criar arquivo de exemplo
echo "DESAFIO-HYPERATIVA           20180524LOTE0001000002" > cards.txt
echo "C1     4532015112830366" >> cards.txt
echo "C2     4000000000000002" >> cards.txt
echo "LOTE0001000002" >> cards.txt

# Upload do arquivo
curl -X POST http://localhost:8080/cards/batch \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@cards.txt"
```

**Resposta:**
```json
{
  "processedCards": 2,
  "successfulCards": 2,
  "failedCards": 0,
  "errors": []
}
```

## 📋 Testando com Postman/Insomnia

1. **Subir aplicação e banco:**
```bash
docker-compose up --build
```

2. **Importar collection:**
   - Postman: Importar `collection/Credit-Card-API.postman_collection.json`
   - Insomnia: Importar o mesmo arquivo

3. **Testar endpoints:**
   - Execute "Login" primeiro para obter o token
   - O token será automaticamente usado nos outros endpoints
   - Teste "Create Card", "Get Card" e "Batch Upload"

## 📋 Exemplo de Uso (cURL)

```bash
# Login
TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | jq -r '.token')

# Criar cartão
curl -X POST http://localhost:8080/cards \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"cardNumber":"4532015112830366"}'

# Buscar cartão
curl -X GET http://localhost:8080/cards/4532015112830366 \
  -H "Authorization: Bearer $TOKEN"

# Upload em lote
curl -X POST http://localhost:8080/cards/batch \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@examples/sample-cards.txt"
```