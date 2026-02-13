# 🚀 Quick Start - 5 Minutos

## 1. Inicie a aplicação

```bash
./mvnw quarkus:dev
```

Aguarde até ver: `Quarkus X.X.X started in X.XXXs`

## 2. Registre um usuário

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "joao",
    "email": "joao@example.com",
    "password": "senha123"
  }'
```

**Resposta esperada:**
```json
{
  "token": "eyJhbGc...",
  "username": "joao",
  "email": "joao@example.com"
}
```

## 3. Copie o token e teste uma rota protegida

```bash
curl -X GET http://localhost:8080/api/protected/user \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

**Resposta esperada:**
```json
{
  "username": "joao",
  "email": "joao@example.com",
  "userId": 1,
  "roles": ["USER"],
  "message": "Hello joao, you have access to this protected route!"
}
```

## 4. Teste o script automatizado (opcional)

```bash
./test-api.sh
```

---

## 📍 Endpoints Disponíveis

| Endpoint | Método | Auth | Descrição |
|----------|--------|------|-----------|
| `/api/auth/register` | POST | ❌ | Registra novo usuário |
| `/api/auth/login` | POST | ❌ | Faz login |
| `/api/protected/user` | GET | ✅ | Info do usuário (requer USER ou ADMIN) |
| `/api/protected/admin` | GET | ✅ | Rota admin (requer ADMIN) |
| `/api/protected/public` | GET | ❌ | Rota pública |

---

## 🔐 Como Proteger Suas Rotas

Adicione `@RolesAllowed` no seu endpoint:

```java
@GET
@Path("/my-route")
@RolesAllowed("USER")  // Apenas usuários autenticados
public Response myRoute() {
    return Response.ok("Protected!").build();
}
```

## 📖 Mais Informações

- **README.md** - Documentação completa
- **EXAMPLES.md** - Exemplos em várias linguagens
- **test-api.sh** - Script de teste automatizado

## 🆘 Problemas?

1. Verifique se a porta 8080 está livre
2. Confirme que o Java 17+ está instalado
3. Execute `./mvnw clean compile` se houver erros

**Pronto! Sua API de autenticação está funcionando! 🎉**
