#!/bin/bash

# Cores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

BASE_URL="http://localhost:8080"

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Testes de Validação${NC}"
echo -e "${BLUE}========================================${NC}\n"

# Teste 1: Username vazio
echo -e "${YELLOW}1. Username vazio (deve falhar)${NC}"
curl -s -X POST "${BASE_URL}/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "",
    "email": "test@example.com",
    "password": "senha123"
  }' | jq .
echo -e "\n"

# Teste 2: Username muito curto
echo -e "${YELLOW}2. Username muito curto (deve falhar)${NC}"
curl -s -X POST "${BASE_URL}/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "ab",
    "email": "test@example.com",
    "password": "senha123"
  }' | jq .
echo -e "\n"

# Teste 3: Username com caracteres inválidos
echo -e "${YELLOW}3. Username com caracteres inválidos (deve falhar)${NC}"
curl -s -X POST "${BASE_URL}/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "joão@123",
    "email": "test@example.com",
    "password": "senha123"
  }' | jq .
echo -e "\n"

# Teste 4: Email inválido
echo -e "${YELLOW}4. Email inválido (deve falhar)${NC}"
curl -s -X POST "${BASE_URL}/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "joao",
    "email": "email-invalido",
    "password": "senha123"
  }' | jq .
echo -e "\n"

# Teste 5: Senha muito curta
echo -e "${YELLOW}5. Senha muito curta (deve falhar)${NC}"
curl -s -X POST "${BASE_URL}/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "joao",
    "email": "joao@example.com",
    "password": "123"
  }' | jq .
echo -e "\n"

# Teste 6: Múltiplos erros
echo -e "${YELLOW}6. Múltiplos erros de validação (deve falhar)${NC}"
curl -s -X POST "${BASE_URL}/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "ab",
    "email": "invalido",
    "password": "123"
  }' | jq .
echo -e "\n"

# Teste 7: Campos faltando
echo -e "${YELLOW}7. Campos obrigatórios faltando (deve falhar)${NC}"
curl -s -X POST "${BASE_URL}/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{}' | jq .
echo -e "\n"

# Teste 8: Dados válidos
echo -e "${YELLOW}8. Dados válidos (deve passar)${NC}"
RESPONSE=$(curl -s -X POST "${BASE_URL}/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "valid_user",
    "email": "valid@example.com",
    "password": "senha123"
  }')

echo "$RESPONSE" | jq .

if echo "$RESPONSE" | jq -e '.token' > /dev/null; then
  echo -e "${GREEN}✓ Validação passou corretamente!${NC}\n"
else
  echo -e "${RED}✗ Erro inesperado${NC}\n"
fi

# Teste 9: Login com username curto
echo -e "${YELLOW}9. Login com username curto (deve falhar)${NC}"
curl -s -X POST "${BASE_URL}/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "ab",
    "password": "senha123"
  }' | jq .
echo -e "\n"

# Teste 10: Login com senha curta
echo -e "${YELLOW}10. Login com senha curta (deve falhar)${NC}"
curl -s -X POST "${BASE_URL}/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "valid_user",
    "password": "123"
  }' | jq .
echo -e "\n"

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  Testes de Validação Concluídos!${NC}"
echo -e "${GREEN}========================================${NC}"
