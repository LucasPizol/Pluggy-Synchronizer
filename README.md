# Open Finance Transaction Sync

API REST para sincronização de transações bancárias via Open Finance (Pluggy).

## Funcionalidades

- Sincronização de transações bancárias via API Pluggy
- Persistência de transações em PostgreSQL
- Autenticação automática com cache em Redis
- Arquitetura em camadas (Domain, Application, Infrastructure, Gateway)

## Arquitetura

```
src/main/java/com/
├── domain/                          # Regras de negócio e interfaces
│   ├── gateway/
│   │   └── open_finance/
│   │       ├── IOpenFinance.java    # Interface do gateway
│   │       └── models/
│   │           ├── Transaction.java
│   │           └── TransactionPageResponse.java
│   └── services/
│       ├── open_finance/
│       │   └── ITransactionSynchronizer.java
│       └── transactions/
│           └── ITransactionList.java
├── application/                     # Casos de uso
│   └── services/
│       ├── open_finance/
│       │   └── TransactionsSynchronizer.java
│       └── transactions/
│           └── TransactionsList.java
├── infrastructure/                  # Persistência
│   └── persistence/
│       ├── entities/
│       │   └── TransactionEntity.java
│       └── repositories/
│           └── TransactionRepository.java
├── gateway/                         # Integrações externas
│   ├── Pluggy.java                  # Implementação Open Finance
│   └── pluggy/auth/
│       ├── AuthenticationService.java
│       ├── PluggyAuthContext.java
│       ├── PluggyAuthInterceptor.java
│       └── RequiresPluggyAuth.java
└── resources/                       # Endpoints REST
    └── TransactionsSynchronizer.java
```

## Pré-requisitos

- Java 21+
- Maven 3.8+
- Docker (para PostgreSQL e Redis)

## Instalação

### 1. Clone e configure

```bash
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

Sincroniza transações de uma conta bancária da Pluggy para o banco local.

```bash
POST /transactions/sync
Content-Type: application/json

{
  "accountId": "562b795d-1653-429f-be86-74ead9502813"
}
```

**Resposta (200):**
```json
{
  "message": "Transactions synchronized successfully"
}
```

### Listar Transações

Lista transações armazenadas no banco local.

```bash
GET /transactions?accountId=562b795d-1653-429f-be86-74ead9502813
```

**Resposta (200):**
```json
{
  "total": 10,
  "page": 1,
  "data": [
    {
      "id": "a8534c85-53ce-4f21-94d7-50e9d2ee4957",
      "accountId": "562b795d-1653-429f-be86-74ead9502813",
      "description": "PAGO NETFLIX SERV",
      "amount": -58.00,
      "date": "2020-10-15T00:00:00",
      "status": "POSTED",
      "type": "DEBIT",
      "categoryId": 1
    }
  ]
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
- **PostgreSQL** - Banco de dados
- **Redis** - Cache de tokens de autenticação
- **Jackson** - Serialização JSON
- **SmallRye JWT** - Autenticação JWT

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

Verifique se as credenciais da Pluggy no `.env` estão corretas e sem aspas.

### Erro: "Unsatisfied dependency"

Adicione `@ApplicationScoped` na classe que implementa a interface.

## Licença

MIT
