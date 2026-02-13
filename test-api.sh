#!/bin/bash

# Cores para output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

BASE_URL="http://localhost:8080"

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Quarkus Authentication Test Script${NC}"
echo -e "${BLUE}========================================${NC}\n"

# 1. Test Public Route
echo -e "${YELLOW}1. Testing public route (no auth required)${NC}"
curl -X GET "${BASE_URL}/api/protected/public" \
  -H "Content-Type: application/json" \
  -w "\n\n"

# 2. Register a new user
echo -e "${YELLOW}2. Registering new user${NC}"
REGISTER_RESPONSE=$(curl -s -X POST "${BASE_URL}/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "joao",
    "email": "joao@example.com",
    "password": "senha123"
  }')

echo "$REGISTER_RESPONSE" | jq .
TOKEN=$(echo "$REGISTER_RESPONSE" | jq -r '.token')
echo -e "${GREEN}Token received: ${TOKEN:0:50}...${NC}\n"

# 3. Try to register same user (should fail)
echo -e "${YELLOW}3. Trying to register same user (should fail)${NC}"
curl -X POST "${BASE_URL}/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "joao",
    "email": "joao@example.com",
    "password": "senha123"
  }' \
  -w "\n\n" | jq .

# 4. Login with the user
echo -e "${YELLOW}4. Logging in with credentials${NC}"
LOGIN_RESPONSE=$(curl -s -X POST "${BASE_URL}/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "joao",
    "password": "senha123"
  }')

echo "$LOGIN_RESPONSE" | jq .
TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.token')
echo -e "${GREEN}New token received: ${TOKEN:0:50}...${NC}\n"

# 5. Access protected route with token
echo -e "${YELLOW}5. Accessing protected route WITH valid token${NC}"
curl -X GET "${BASE_URL}/api/protected/user" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${TOKEN}" \
  -w "\n\n" | jq .

# 6. Try to access protected route without token (should fail)
echo -e "${YELLOW}6. Accessing protected route WITHOUT token (should fail)${NC}"
curl -X GET "${BASE_URL}/api/protected/user" \
  -H "Content-Type: application/json" \
  -w "\n\n"

# 7. Try to access admin route (should fail - user doesn't have ADMIN role)
echo -e "${YELLOW}7. Accessing admin route with USER role (should fail)${NC}"
curl -X GET "${BASE_URL}/api/protected/admin" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${TOKEN}" \
  -w "\n\n"

# 8. Try login with wrong password
echo -e "${YELLOW}8. Trying to login with wrong password (should fail)${NC}"
curl -X POST "${BASE_URL}/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "joao",
    "password": "wrongpassword"
  }' \
  -w "\n\n" | jq .

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  All tests completed!${NC}"
echo -e "${GREEN}========================================${NC}"
