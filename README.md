# 🔐 Quarkus Authentication Quick Start

Sistema de autenticação completo com JWT para Quarkus.

> **📖 Documentação Rápida:**
> - **[QUICKSTART.md](QUICKSTART.md)** - Comece em 5 minutos
> - **[EXAMPLES.md](EXAMPLES.md)** - Exemplos em várias linguagens
> - **[VALIDATION.md](VALIDATION.md)** - Validação automática de dados
> - **[SECURITY.md](SECURITY.md)** - Considerações de segurança

## 🚀 Funcionalidades

- ✅ Registro de usuários
- ✅ Login com JWT
- ✅ Senhas criptografadas com BCrypt
- ✅ Proteção de rotas com `@RolesAllowed`
- ✅ Roles de usuário (USER, ADMIN)
- ✅ **Validação automática de dados com Bean Validation**

## 📋 Pré-requisitos

- Java 17+
- Maven 3.8+

## 🔧 Instalação

1. Instale as dependências:

```bash
mvn clean install
```

2. Execute a aplicação:

```bash
mvn quarkus:dev
```

A aplicação estará disponível em `http://localhost:8080`

## 🔐 Endpoints da API

### Autenticação

#### Registrar Novo Usuário

```bash
POST /api/auth/register
Content-Type: application/json

{
  "username": "joao",
  "email": "joao@example.com",
  "password": "senha123"
}
```

**Resposta de Sucesso (201):**
```json
{
  "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "joao",
  "email": "joao@example.com"
}
```

#### Login

```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "joao",
  "password": "senha123"
}
```

**Resposta de Sucesso (200):**
```json
{
  "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "joao",
  "email": "joao@example.com"
}
```

### Rotas Protegidas

#### Informações do Usuário (Requer Autenticação)

```bash
GET /api/protected/user
Authorization: Bearer SEU_TOKEN_JWT
```

**Resposta:**
```json
{
  "username": "joao",
  "email": "joao@example.com",
  "userId": 1,
  "roles": ["USER"],
  "message": "Hello joao, you have access to this protected route!"
}
```

#### Rota Admin (Requer Role ADMIN)

```bash
GET /api/protected/admin
Authorization: Bearer SEU_TOKEN_JWT_ADMIN
```

#### Rota Pública (Sem Autenticação)

```bash
GET /api/protected/public
```

## 🧪 Testando com cURL

### 1. Registrar um usuário

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "joao",
    "email": "joao@example.com",
    "password": "senha123"
  }'
```

### 2. Fazer login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "joao",
    "password": "senha123"
  }'
```

**Copie o token da resposta!**

### 3. Acessar rota protegida

```bash
curl -X GET http://localhost:8080/api/protected/user \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

## 🔒 Como Proteger Suas Rotas

Para proteger um endpoint, basta adicionar a anotação `@RolesAllowed`:

```java
@GET
@Path("/minha-rota")
@RolesAllowed({"USER"})
public Response minhaRota() {
    // Código protegido
}
```

### Níveis de Proteção

- **Sem anotação**: Público (qualquer um pode acessar)
- **`@RolesAllowed("USER")`**: Apenas usuários autenticados
- **`@RolesAllowed("ADMIN")`**: Apenas administradores
- **`@RolesAllowed({"USER", "ADMIN"})`**: Usuários ou admins

## 📝 Acessando Informações do Token

Você pode injetar o JWT em qualquer recurso:

```java
@Inject
JsonWebToken jwt;

public String getUsername() {
    return jwt.getName(); // Retorna o username
}

public Object getClaim() {
    return jwt.getClaim("email"); // Acessa claims customizados
}

public Set<String> getRoles() {
    return jwt.getGroups(); // Retorna as roles
}
```

## ⚙️ Configuração

Veja o arquivo `src/main/resources/application.properties`:

```properties
# Duração do token em segundos (padrão: 24 horas)
jwt.duration=86400

# Porta da aplicação
quarkus.http.port=8080
```

## 🗂️ Estrutura do Projeto

```
src/main/java/com/
├── application/
│   ├── dto/              # Data Transfer Objects
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   ├── AuthResponse.java
│   │   └── UserDTO.java
│   └── service/          # Lógica de negócio
│       └── AuthService.java
├── domain/
│   └── models/           # Entidades
│       └── User.java
├── infrastructure/
│   └── repository/       # Persistência
│       └── UserRepository.java
└── resources/            # Controllers REST
    ├── SessionResource.java
    └── ProtectedResource.java
```

## 🔑 Segurança

- ✅ Senhas são criptografadas com BCrypt (salt automático)
- ✅ Tokens JWT assinados com RSA-256
- ✅ Validação de dados de entrada
- ✅ Proteção contra duplicação de username/email

## 📦 Para Produção

**Importante:** Este é um quickstart com armazenamento em memória. Para produção:

1. **Adicione um banco de dados:**
   - Hibernate com Panache
   - PostgreSQL, MySQL, etc.

2. **Adicione validações:**
   ```xml
   <dependency>
     <groupId>io.quarkus</groupId>
     <artifactId>quarkus-hibernate-validator</artifactId>
   </dependency>
   ```

3. **Configure CORS adequadamente** em `application.properties`

4. **Use variáveis de ambiente** para informações sensíveis

5. **Implemente refresh tokens** para melhor segurança

## 🐛 Troubleshooting

### Erro: "Invalid token"
- Verifique se está enviando o header `Authorization: Bearer TOKEN`
- Confirme que o token não expirou (duração configurada em `jwt.duration`)

### Erro: "Forbidden"
- Verifique se seu usuário tem a role necessária para acessar a rota

## 📚 Próximos Passos

- [ ] Adicionar refresh tokens
- [ ] Implementar "esqueci minha senha"
- [ ] Adicionar verificação de email
- [ ] Implementar rate limiting
- [ ] Adicionar logs de auditoria
- [ ] Integrar com banco de dados

## 📖 Documentação Oficial

- [Quarkus Security](https://quarkus.io/guides/security)
- [SmallRye JWT](https://quarkus.io/guides/security-jwt)
- [Quarkus REST](https://quarkus.io/guides/rest-json)

## 📄 Licença

MIT
