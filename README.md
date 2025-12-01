# Credit Card API - Secure Registration and Consultation

Uma API REST segura para cadastro e consulta de números completos de cartão de crédito, implementada com **Java 21**, **Spring Boot 3.x** e **Arquitetura Hexagonal**.

## 🏗️ Arquitetura

### Arquitetura Hexagonal (Ports & Adapters)

```
┌─────────────────────────────────────────────────────────────┐
│                    ADAPTERS (IN)                            │
│  ┌─────────────────┐  ┌─────────────────┐                  │
│  │   REST API      │  │   Swagger UI    │                  │
│  │  Controllers    │  │   Documentation │                  │
│  └─────────────────┘  └─────────────────┘                  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   APPLICATION LAYER                         │
│  ┌─────────────────┐  ┌─────────────────┐                  │
│  │   Use Cases     │  │   Services      │                  │
│  │  (Business      │  │   (Ports)       │                  │
│  │   Logic)        │  │                 │                  │
│  └─────────────────┘  └─────────────────┘                  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    DOMAIN LAYER                             │
│  ┌─────────────────┐  ┌─────────────────┐                  │
│  │   Entities      │  │  Value Objects  │                  │
│  │   (Card)        │  │  (CardNumber,   │                  │
│  │                 │  │   CardId)       │                  │
│  └─────────────────┘  └─────────────────┘                  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   ADAPTERS (OUT)                            │
│  ┌─────────────────┐  ┌─────────────────┐                  │
│  │   Database      │  │   Encryption    │                  │
│  │  (MySQL/JPA)    │  │   (AES-256)     │                  │
│  └─────────────────┘  └─────────────────┘                  │
└─────────────────────────────────────────────────────────────┘
```

### Estrutura de Pacotes

```
com.creditcard/
├── domain/                    # Camada de Domínio
│   ├── card/                 # Agregado Card
│   │   ├── Card.java         # Entidade
│   │   ├── CardId.java       # Value Object
│   │   ├── CardNumber.java   # Value Object
│   │   └── CardRepository.java # Port
│   ├── auth/                 # Agregado Auth
│   └── shared/               # Objetos compartilhados
├── application/              # Camada de Aplicação
│   ├── card/                # Use Cases de Card
│   └── auth/                # Use Cases de Auth
├── adapters/                # Camada de Adaptadores
│   ├── in/web/              # Adaptadores de Entrada
│   └── out/persistence/     # Adaptadores de Saída
└── config/                  # Configurações
```

## 🚀 Tecnologias

- **Java 21** - Linguagem de programação
- **Spring Boot 3.2.0** - Framework principal
- **Spring Security** - Autenticação e autorização
- **JWT** - Tokens de acesso
- **MySQL 8** - Banco de dados
- **JPA/Hibernate** - ORM
- **Flyway** - Migração de banco
- **HikariCP** - Pool de conexões
- **AES-256-GCM** - Criptografia
- **HMAC-SHA256** - Hash determinístico
- **Swagger/OpenAPI** - Documentação
- **JUnit 5** - Testes unitários
- **Testcontainers** - Testes de integração
- **Docker** - Containerização

## 🔒 Segurança

### Criptografia
- **AES-256-GCM**: Criptografia dos números de cartão
- **HMAC-SHA256**: Hash determinístico para busca exata
- **BCrypt**: Hash das senhas de usuário
- **JWT**: Tokens de autenticação com expiração de 1 hora

### Autenticação
- Usuário padrão: `admin` / `admin123`
- Todas as rotas protegidas exceto `/auth/login`
- Headers de autorização obrigatórios: `Authorization: Bearer <token>`

## 📊 Endpoints

### Autenticação
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

### Cartões
```http
# Criar cartão
POST /api/v1/cards
Authorization: Bearer <token>
Content-Type: application/json

{
  "cardNumber": "4532015112830366"
}

# Buscar cartão
GET /api/v1/cards/{cardNumber}
Authorization: Bearer <token>

# Upload em lote
POST /api/v1/cards/batch
Authorization: Bearer <token>
Content-Type: multipart/form-data

file: arquivo.txt (uma linha por cartão)
```

## 🏃‍♂️ Como Executar

### Pré-requisitos
- Java 21
- Maven 3.8+
- Docker & Docker Compose (para execução containerizada)

### Execução Local

1. **Clone o repositório**
```bash
git clone <repository-url>
cd api-cadastro-consulta-de-numero-de-cartao-credito
```

2. **Configure o banco MySQL**
```bash
# Usando Docker
docker run --name mysql-creditcard \
  -e MYSQL_ROOT_PASSWORD=password \
  -e MYSQL_DATABASE=creditcard_db \
  -p 3306:3306 \
  -d mysql:8.0
```

3. **Configure as variáveis de ambiente**
```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=creditcard_db
export DB_USERNAME=root
export DB_PASSWORD=password
export JWT_SECRET=mySecretKey123456789012345678901234567890
export ENCRYPTION_KEY=myEncryptionKey1234567890123456
```

4. **Execute a aplicação**
```bash
./mvnw spring-boot:run
```

### Execução com Docker

```bash
# Build e execução completa
docker-compose up --build

# Apenas execução (após build)
docker-compose up

# Execução em background
docker-compose up -d

# Parar serviços
docker-compose down

# Parar e remover volumes
docker-compose down -v
```

## 🧪 Testes

### Executar todos os testes
```bash
./mvnw test
```

### Executar com cobertura
```bash
./mvnw clean test jacoco:report
```

### Ver relatório de cobertura
```bash
open target/site/jacoco/index.html
```

### Testes de integração com Testcontainers
```bash
./mvnw test -Dtest=*IntegrationTest
```

## 📋 Exemplos de Uso

### 1. Autenticação
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 2. Criar Cartão
```bash
curl -X POST http://localhost:8080/api/v1/cards \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "cardNumber": "4532015112830366"
  }'
```

**Response:**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000"
}
```

### 3. Buscar Cartão
```bash
curl -X GET http://localhost:8080/api/v1/cards/4532015112830366 \
  -H "Authorization: Bearer <token>"
```

**Response:**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000"
}
```

### 4. Upload em Lote
```bash
# Criar arquivo cards.txt
echo "4532015112830366" > cards.txt
echo "5555555555554444" >> cards.txt
echo "378282246310005" >> cards.txt

# Upload
curl -X POST http://localhost:8080/api/v1/cards/batch \
  -H "Authorization: Bearer <token>" \
  -F "file=@cards.txt"
```

**Response:**
```json
{
  "cardsProcessed": 3
}
```

## 📚 Documentação API

### Swagger UI
Acesse: http://localhost:8080/api/v1/swagger-ui.html

### OpenAPI JSON
Acesse: http://localhost:8080/api/v1/api-docs

## 📈 Monitoramento

### Health Check
```bash
curl http://localhost:8080/api/v1/actuator/health
```

### Métricas
```bash
curl http://localhost:8080/api/v1/actuator/metrics
```

### Prometheus
```bash
curl http://localhost:8080/api/v1/actuator/prometheus
```

## 🔧 Configurações

### Variáveis de Ambiente

| Variável | Descrição | Padrão |
|----------|-----------|---------|
| `DB_HOST` | Host do MySQL | `localhost` |
| `DB_PORT` | Porta do MySQL | `3306` |
| `DB_NAME` | Nome do banco | `creditcard_db` |
| `DB_USERNAME` | Usuário do banco | `root` |
| `DB_PASSWORD` | Senha do banco | `password` |
| `JWT_SECRET` | Chave secreta JWT | `mySecretKey...` |
| `ENCRYPTION_KEY` | Chave de criptografia | `myEncryptionKey...` |

### Profiles Spring

- **default**: Desenvolvimento local
- **docker**: Execução em container
- **test**: Execução de testes

## 🏛️ Decisões Técnicas

### Arquitetura Hexagonal
- **Isolamento do domínio**: Regras de negócio independentes de frameworks
- **Testabilidade**: Fácil criação de mocks e testes unitários
- **Flexibilidade**: Troca de adaptadores sem impacto no domínio

### Criptografia AES-256-GCM
- **Confidencialidade**: Números de cartão criptografados no banco
- **Integridade**: GCM fornece autenticação integrada
- **Performance**: Hash HMAC-SHA256 para busca rápida

### Validação Luhn
- **Conformidade**: Algoritmo padrão da indústria
- **Detecção de erros**: Identifica números inválidos
- **Segurança**: Primeira linha de defesa

### Logging Estruturado
- **Observabilidade**: Logs em formato JSON
- **Rastreabilidade**: Request ID em todas as operações
- **Segurança**: Mascaramento automático de dados sensíveis

### Batch Processing
- **Escalabilidade**: Processamento em streaming
- **Performance**: Inserção em lotes de 1000 registros
- **Resiliência**: Tratamento individual de erros

### Testes com Testcontainers
- **Realismo**: Testes com banco real
- **Isolamento**: Containers descartáveis
- **CI/CD**: Execução em qualquer ambiente

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👥 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📞 Suporte

Para suporte, envie um email para support@creditcardapi.com ou abra uma issue no GitHub.