# 🔍 Validação de Dados

A API usa **Bean Validation (Hibernate Validator)** para validar automaticamente os dados das requisições.

## 📋 Validações Implementadas

### LoginRequest

```java
@NotBlank(message = "Username is required")
@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
public String username;

@NotBlank(message = "Password is required")
@Size(min = 6, message = "Password must be at least 6 characters")
public String password;
```

### RegisterRequest

```java
@NotBlank(message = "Username is required")
@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
@Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Username can only contain letters, numbers, underscores and hyphens")
public String username;

@NotBlank(message = "Email is required")
@Email(message = "Email must be valid")
@Size(max = 100, message = "Email must not exceed 100 characters")
public String email;

@NotBlank(message = "Password is required")
@Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
public String password;
```

## 🧪 Exemplos de Resposta de Validação

### ❌ Requisição Inválida - Campo Vazio

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "",
    "email": "test@example.com",
    "password": "123"
  }'
```

**Resposta (400 Bad Request):**
```json
{
  "message": "Validation failed",
  "errors": [
    {
      "field": "username",
      "message": "Username is required"
    },
    {
      "field": "password",
      "message": "Password must be at least 6 characters"
    }
  ]
}
```

### ❌ Email Inválido

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "joao",
    "email": "email-invalido",
    "password": "senha123"
  }'
```

**Resposta (400 Bad Request):**
```json
{
  "message": "Validation failed",
  "errors": [
    {
      "field": "email",
      "message": "Email must be valid"
    }
  ]
}
```

### ❌ Username com Caracteres Inválidos

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "joão@123",
    "email": "joao@example.com",
    "password": "senha123"
  }'
```

**Resposta (400 Bad Request):**
```json
{
  "message": "Validation failed",
  "errors": [
    {
      "field": "username",
      "message": "Username can only contain letters, numbers, underscores and hyphens"
    }
  ]
}
```

### ❌ Múltiplos Erros

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "ab",
    "email": "invalido",
    "password": "123"
  }'
```

**Resposta (400 Bad Request):**
```json
{
  "message": "Validation failed",
  "errors": [
    {
      "field": "username",
      "message": "Username must be between 3 and 50 characters"
    },
    {
      "field": "email",
      "message": "Email must be valid"
    },
    {
      "field": "password",
      "message": "Password must be at least 6 characters"
    }
  ]
}
```

### ✅ Requisição Válida

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "joao",
    "email": "joao@example.com",
    "password": "senha123"
  }'
```

**Resposta (201 Created):**
```json
{
  "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "joao",
  "email": "joao@example.com"
}
```

## 📝 Anotações de Validação Disponíveis

### Validações Básicas
- `@NotNull` - Não pode ser null
- `@NotBlank` - Não pode ser null, vazio ou apenas espaços
- `@NotEmpty` - Não pode ser null ou vazio

### Strings
- `@Size(min, max)` - Tamanho da string
- `@Email` - Validação de email
- `@Pattern(regexp)` - Expressão regular
- `@Min(value)` - Valor mínimo (números)
- `@Max(value)` - Valor máximo (números)

### Exemplos Adicionais

```java
// CPF/CNPJ
@Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", 
         message = "CPF must be in format XXX.XXX.XXX-XX")
public String cpf;

// Telefone
@Pattern(regexp = "^\\(\\d{2}\\) \\d{5}-\\d{4}$", 
         message = "Phone must be in format (XX) XXXXX-XXXX")
public String phone;

// Data futura
@Future(message = "Date must be in the future")
public LocalDate eventDate;

// Número positivo
@Positive(message = "Value must be positive")
public BigDecimal amount;

// Range
@Min(value = 18, message = "Must be at least 18 years old")
@Max(value = 120, message = "Must be at most 120 years old")
public Integer age;
```

## 🔧 Como Adicionar Validação em Novos DTOs

1. **Adicione as anotações** no DTO:
```java
public class MyRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    public String name;
}
```

2. **Use `@Valid`** no endpoint:
```java
@POST
public Response create(@Valid MyRequest request) {
    // Validação automática acontece antes de chegar aqui
    return Response.ok().build();
}
```

3. **Pronto!** O framework valida automaticamente e retorna erros formatados.

## 🎨 Customizando Mensagens

### Mensagens em Português

```java
@NotBlank(message = "Nome é obrigatório")
@Size(min = 3, max = 50, message = "Nome deve ter entre 3 e 50 caracteres")
public String name;
```

### Usando Arquivo de Mensagens

Crie `src/main/resources/ValidationMessages.properties`:

```properties
username.required=Nome de usuário é obrigatório
username.size=Nome de usuário deve ter entre {min} e {max} caracteres
email.invalid=Email inválido
password.min=Senha deve ter no mínimo {value} caracteres
```

Use no DTO:
```java
@NotBlank(message = "{username.required}")
@Size(min = 3, max = 50, message = "{username.size}")
public String username;
```

## 🧩 Validação de Grupos

Para validações condicionais:

```java
public interface Create {}
public interface Update {}

public class UserDTO {
    @Null(groups = Create.class)
    @NotNull(groups = Update.class)
    public Long id;
    
    @NotBlank(groups = {Create.class, Update.class})
    public String username;
}

// No endpoint
@POST
public Response create(@Valid(groups = Create.class) UserDTO dto) { }

@PUT
public Response update(@Valid(groups = Update.class) UserDTO dto) { }
```

## 🔍 Validação Customizada

Para regras de negócio complexas:

```java
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
public @interface ValidUsername {
    String message() default "Username is invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {
    @Inject
    UserRepository userRepository;
    
    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null) return true;
        return !userRepository.existsByUsername(username);
    }
}

// Uso
@ValidUsername(message = "Username already exists")
public String username;
```

## 📚 Referências

- [Jakarta Bean Validation](https://beanvalidation.org/)
- [Hibernate Validator Docs](https://hibernate.org/validator/)
- [Quarkus Validation Guide](https://quarkus.io/guides/validation)
