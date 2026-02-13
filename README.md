# Open Finance Transaction Sync

API REST para sincronização de transações bancárias via Open Finance utilizando a API Pluggy.

## Funcionalidades

- Sincronização automática de transações bancárias via API Pluggy
- Persistência de contas, items de conta e transações em PostgreSQL
- Autenticação automática com cache de tokens em Redis
- API RESTful com paginação
- Arquitetura em camadas (Clean Architecture)

## Arquitetura

```
src/main/java/com/
├── application/                         # Casos de uso e DTOs
│   ├── dto/
│   │   ├── AccountDTO.java
│   │   ├── AccountItemDTO.java
│   │   └── TransactionDTO.java
│   └── usecase/
│       ├── accountconnection/
│       │   ├── CreateAccountUseCase.java
│       │   └── GetAccountUseCase.java
│       ├── accountitem/
│       │   ├── CreateAccountItemUseCase.java
│       │   └── ListAccountItemUseCase.java
│       ├── openfinance/
│       │   └── TransactionsSynchronizerUseCase.java
│       └── transactions/
│           └── TransactionsListUseCase.java
├── domain/                              # Interfaces e modelos de domínio
│   ├── gateway/openfinance/
│   │   ├── IOpenFinance.java
│   │   └── models/
│   │       ├── OpenFinanceAccount.java
│   │       ├── OpenFinanceAccountItem.java
│   │       └── OpenFinanceTransaction.java
│   ├── shared/
│   │   └── PaginatedResponse.java
│   └── usecase/                         # Interfaces dos casos de uso
├── infrastructure/                      # Implementações de infraestrutura
│   ├── gateway/pluggy/
│   │   ├── PluggyGateway.java           # Implementação Open Finance
│   │   ├── auth/
│   │   │   ├── AuthenticationService.java
│   │   │   ├── PluggyAuthContext.java
│   │   │   ├── PluggyAuthInterceptor.java
│   │   │   └── RequiresPluggyAuth.java
│   │   └── dto/                         # DTOs da API Pluggy
│   └── persistence/
│       ├── entities/
│       │   ├── AccountEntity.java
│       │   ├── AccountItemEntity.java
│       │   └── TransactionEntity.java
│       └── repositories/
│           ├── AccountRepository.java
│           ├── AccountItemRepository.java
│           └── TransactionRepository.java
└── resources/                           # Endpoints REST
    ├── AccountConnection.java
    └── TransactionsSynchronizer.java
```

## Modelo de Dados

```
Account (Conexão bancária)
├── AccountItem (Conta corrente, poupança, etc.)
│   └── Transaction (Transações da conta)
```

## Pré-requisitos

- Java 21+
- Maven 3.8+
- Docker (para PostgreSQL e Redis)

## Instalação

### 1. Clone e configure

```bash
git clone <repo-url>
cd rest-json-quickstart
cp .env.example .env
# Edite o .env com suas credenciais da Pluggy
```

### 2. Inicie os serviços

```bash
# PostgreSQL
docker run -d \
  --name postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=transactions_db \
  -p 5432:5432 \
  postgres:16

# Redis
docker run -d \
  --name redis \
  -p 6379:6379 \
  redis:7
```

### 3. Execute a aplicação

```bash
./mvnw quarkus:dev
```

A aplicação estará disponível em `http://localhost:8080`

## Configuração

### Variáveis de Ambiente (.env)

```bash
# Pluggy API (Open Finance)
PLUGGY_API_URL=https://api.pluggy.ai
PLUGGY_CLIENT_ID=seu_client_id
PLUGGY_CLIENT_SECRET=seu_client_secret
PLUGGY_AUTH_CACHE_TTL=3600

# PostgreSQL
POSTGRES_HOST=localhost
POSTGRES_PORT=5432
POSTGRES_DB=transactions_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

# Redis (cache de tokens)
REDIS_HOST=localhost
REDIS_PORT=6379

# Hibernate
HIBERNATE_DDL=update
HIBERNATE_LOG_SQL=false
```

## Endpoints

### Sincronizar Transações

Sincroniza transações de uma conexão bancária da Pluggy para o banco local.

```bash
POST /transactions/sync
Content-Type: application/json

{
  "accountId": "fc07dc28-8372-457d-a8b8-5af0716c09df"
}
```

**Resposta (200):**
```json
{
  "message": "Transactions synchronized successfully"
}
```

### Listar Transações

Lista transações com paginação.

```bash
GET /transactions?accountId={accountItemId}&page=1&pageSize=10
```

**Resposta (200):**
```json
{
  "page": 1,
  "pageSize": 10,
  "total": 8,
  "totalPages": 1,
  "items": [
    {
      "id": "f3444523-eb46-446a-9f16-aa7743c48a09",
      "description": "PAGO NETFLIX SERV",
      "amount": -58.0,
      "date": "2020-10-15T00:00:00",
      "status": "POSTED",
      "type": "DEBIT",
      "categoryId": 1,
      "accountItem": {
        "id": "1eba60e2-51c7-4b04-95a9-2af01b11c5a6",
        "integrationId": "a3fb5ebf-4cf7-4726-b7c6-a0d659af401b",
        "name": "Conta Corrente",
        "account": {
          "id": "3b5a6be1-be43-4a68-bb3c-baf1a0c8c9d2",
          "name": "Nubank",
          "primaryColor": "8a0fbe",
          "institutionLogo": "https://cdn.pluggy.ai/assets/connector-icons/212.svg",
          "institutionId": 612
        }
      }
    }
  ]
}
```

### Buscar Conexão

Busca informações de uma conexão bancária.

```bash
GET /account-connection?accountId={accountId}
```

**Resposta (200):**
```json
{
  "id": "3b5a6be1-be43-4a68-bb3c-baf1a0c8c9d2",
  "name": "Nubank",
  "primaryColor": "8a0fbe",
  "institutionLogo": "https://cdn.pluggy.ai/assets/connector-icons/212.svg",
  "institutionId": 612,
  "accountId": "fc07dc28-8372-457d-a8b8-5af0716c09df"
}
```

## Fluxo de Autenticação Pluggy

A autenticação com a API Pluggy é gerenciada automaticamente:

1. **Interceptor** (`@RequiresPluggyAuth`) intercepta chamadas que precisam de autenticação
2. **AuthenticationService** obtém o token (do cache Redis ou nova autenticação)
3. **PluggyAuthContext** armazena o token durante a requisição
4. Token é cacheado no Redis pelo TTL configurado (padrão: 1 hora)

## Tecnologias

- **Quarkus 3.31** - Framework Java supersônico
- **Hibernate ORM + Panache** - Persistência simplificada
- **PostgreSQL** - Banco de dados relacional
- **Redis** - Cache de tokens de autenticação
- **Jackson** - Serialização JSON
- **Bean Validation** - Validação de requests

## Testes

```bash
# Executar todos os testes
./mvnw test

# Executar com relatório de cobertura
./mvnw test jacoco:report
```

## Desenvolvimento

### Modo Dev (hot reload)

```bash
./mvnw quarkus:dev
```

### Compilar

```bash
./mvnw clean package
```

### Executar JAR

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

## Troubleshooting

### Erro: "database does not exist"

Crie o banco de dados:
```bash
docker exec -it postgres psql -U postgres -c "CREATE DATABASE transactions_db;"
```

### Erro: "release version 17 not supported"

Instale o JDK 21 completo:
```bash
sudo apt install openjdk-21-jdk
```

### Erro: "Authentication failed: 500"

Verifique se as credenciais da Pluggy no `.env` estão corretas e sem aspas extras.

### Erro: "Unsatisfied dependency"

Adicione `@ApplicationScoped` na classe que implementa a interface.

### Reset do banco de dados

```bash
docker exec postgres psql -U postgres -c "DROP DATABASE IF EXISTS transactions_db;"
docker exec postgres psql -U postgres -c "CREATE DATABASE transactions_db;"
```

## Licença

MIT
