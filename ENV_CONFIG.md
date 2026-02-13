# Configuração de Ambiente

Este projeto usa variáveis de ambiente para configuração. Siga os passos abaixo:

## 1. Configurar o arquivo `.env`

Copie o arquivo `.env.example` para `.env`:

```bash
cp .env.example .env
```

## 2. Editar as variáveis

Abra o `.env` e configure conforme necessário:

```bash
# Pluggy API Configuration
PLUGGY_API_URL=https://api.pluggy.ai
PLUGGY_AUTH_CACHE_TTL=3600

# Redis Configuration
REDIS_HOST=localhost
REDIS_PORT=6379

# Application Configuration
QUARKUS_HTTP_PORT=8080
```

## 3. Iniciar o Redis

```bash
docker run -d --name redis -p 6379:6379 redis:alpine
```

## 4. Rodar a aplicação

### Com Maven
```bash
./mvnw quarkus:dev
```

### Ou carregando o .env manualmente
```bash
export $(cat .env | xargs) && ./mvnw quarkus:dev
```

## Variáveis disponíveis

| Variável | Descrição | Padrão |
|----------|-----------|--------|
| `PLUGGY_API_URL` | URL base da API Pluggy | `https://api.pluggy.ai` |
| `PLUGGY_AUTH_CACHE_TTL` | Tempo de cache do token (segundos) | `3600` |
| `REDIS_HOST` | Host do Redis | `localhost` |
| `REDIS_PORT` | Porta do Redis | `6379` |
| `QUARKUS_HTTP_PORT` | Porta da aplicação | `8080` |

## Notas importantes

⚠️ **NUNCA** commite o arquivo `.env` no git! Ele já está no `.gitignore`.

✅ Use o `.env.example` como template para novos ambientes.

✅ Para produção, use variáveis de ambiente do sistema ou secrets management.
