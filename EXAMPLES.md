# 📚 Exemplos de Uso da API

## JavaScript/TypeScript (Fetch API)

```javascript
// Registrar usuário
async function register(username, email, password) {
  const response = await fetch('http://localhost:8080/api/auth/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ username, email, password })
  });
  
  const data = await response.json();
  
  if (response.ok) {
    // Salvar token no localStorage
    localStorage.setItem('token', data.token);
    return data;
  } else {
    throw new Error(data.message);
  }
}

// Login
async function login(username, password) {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ username, password })
  });
  
  const data = await response.json();
  
  if (response.ok) {
    localStorage.setItem('token', data.token);
    return data;
  } else {
    throw new Error(data.message);
  }
}

// Acessar rota protegida
async function getUserInfo() {
  const token = localStorage.getItem('token');
  
  const response = await fetch('http://localhost:8080/api/protected/user', {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    }
  });
  
  if (response.ok) {
    return await response.json();
  } else {
    throw new Error('Unauthorized');
  }
}

// Uso
try {
  await register('joao', 'joao@example.com', 'senha123');
  const userInfo = await getUserInfo();
  console.log(userInfo);
} catch (error) {
  console.error(error.message);
}
```

## React Hooks

```typescript
import { useState, useEffect } from 'react';

// Hook de autenticação
export function useAuth() {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem('token'));

  useEffect(() => {
    if (token) {
      fetchUserInfo();
    }
  }, [token]);

  async function register(username: string, email: string, password: string) {
    const response = await fetch('http://localhost:8080/api/auth/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, email, password })
    });

    const data = await response.json();

    if (response.ok) {
      setToken(data.token);
      localStorage.setItem('token', data.token);
      setUser({ username: data.username, email: data.email });
    } else {
      throw new Error(data.message);
    }
  }

  async function login(username: string, password: string) {
    const response = await fetch('http://localhost:8080/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password })
    });

    const data = await response.json();

    if (response.ok) {
      setToken(data.token);
      localStorage.setItem('token', data.token);
      setUser({ username: data.username, email: data.email });
    } else {
      throw new Error(data.message);
    }
  }

  async function fetchUserInfo() {
    const response = await fetch('http://localhost:8080/api/protected/user', {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    if (response.ok) {
      const data = await response.json();
      setUser(data);
    }
  }

  function logout() {
    setToken(null);
    setUser(null);
    localStorage.removeItem('token');
  }

  return { user, register, login, logout, isAuthenticated: !!token };
}

// Componente de exemplo
function App() {
  const { user, login, logout, isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return (
      <LoginForm onLogin={login} />
    );
  }

  return (
    <div>
      <h1>Welcome, {user?.username}!</h1>
      <button onClick={logout}>Logout</button>
    </div>
  );
}
```

## Python (requests)

```python
import requests

BASE_URL = "http://localhost:8080"

class AuthClient:
    def __init__(self):
        self.token = None
        self.user = None

    def register(self, username, email, password):
        response = requests.post(
            f"{BASE_URL}/api/auth/register",
            json={
                "username": username,
                "email": email,
                "password": password
            }
        )
        
        if response.status_code == 201:
            data = response.json()
            self.token = data["token"]
            self.user = {"username": data["username"], "email": data["email"]}
            return data
        else:
            raise Exception(response.json()["message"])

    def login(self, username, password):
        response = requests.post(
            f"{BASE_URL}/api/auth/login",
            json={
                "username": username,
                "password": password
            }
        )
        
        if response.status_code == 200:
            data = response.json()
            self.token = data["token"]
            self.user = {"username": data["username"], "email": data["email"]}
            return data
        else:
            raise Exception(response.json()["message"])

    def get_user_info(self):
        if not self.token:
            raise Exception("Not authenticated")
        
        response = requests.get(
            f"{BASE_URL}/api/protected/user",
            headers={"Authorization": f"Bearer {self.token}"}
        )
        
        if response.status_code == 200:
            return response.json()
        else:
            raise Exception("Unauthorized")

# Uso
client = AuthClient()

try:
    # Registrar
    client.register("joao", "joao@example.com", "senha123")
    
    # Ou fazer login
    # client.login("joao", "senha123")
    
    # Acessar rota protegida
    user_info = client.get_user_info()
    print(f"User info: {user_info}")
    
except Exception as e:
    print(f"Error: {e}")
```

## Java (HttpClient)

```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthClient {
    private static final String BASE_URL = "http://localhost:8080";
    private String token;
    private HttpClient client;
    private ObjectMapper mapper;

    public AuthClient() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public AuthResponse register(String username, String email, String password) throws Exception {
        String json = String.format(
            "{\"username\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}",
            username, email, password
        );

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/auth/register"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            AuthResponse authResponse = mapper.readValue(response.body(), AuthResponse.class);
            this.token = authResponse.token;
            return authResponse;
        } else {
            throw new Exception("Registration failed: " + response.body());
        }
    }

    public AuthResponse login(String username, String password) throws Exception {
        String json = String.format(
            "{\"username\":\"%s\",\"password\":\"%s\"}",
            username, password
        );

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/auth/login"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            AuthResponse authResponse = mapper.readValue(response.body(), AuthResponse.class);
            this.token = authResponse.token;
            return authResponse;
        } else {
            throw new Exception("Login failed: " + response.body());
        }
    }

    public String getUserInfo() throws Exception {
        if (token == null) {
            throw new Exception("Not authenticated");
        }

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/protected/user"))
            .header("Authorization", "Bearer " + token)
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new Exception("Unauthorized");
        }
    }

    static class AuthResponse {
        public String token;
        public String username;
        public String email;
    }
}
```

## Angular Service

```typescript
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

interface AuthResponse {
  token: string;
  username: string;
  email: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8080/api/auth';
  private tokenSubject = new BehaviorSubject<string | null>(
    localStorage.getItem('token')
  );

  constructor(private http: HttpClient) {}

  register(username: string, email: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/register`, {
      username,
      email,
      password
    }).pipe(
      tap(response => {
        localStorage.setItem('token', response.token);
        this.tokenSubject.next(response.token);
      })
    );
  }

  login(username: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, {
      username,
      password
    }).pipe(
      tap(response => {
        localStorage.setItem('token', response.token);
        this.tokenSubject.next(response.token);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    this.tokenSubject.next(null);
  }

  getToken(): string | null {
    return this.tokenSubject.value;
  }

  isAuthenticated(): boolean {
    return !!this.tokenSubject.value;
  }
}

// HTTP Interceptor para adicionar token automaticamente
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const token = this.authService.getToken();

    if (token) {
      const cloned = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`)
      });
      return next.handle(cloned);
    }

    return next.handle(req);
  }
}
```

## cURL Examples

```bash
# Registrar
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"joao","email":"joao@example.com","password":"senha123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"joao","password":"senha123"}'

# Acessar rota protegida (substitua YOUR_TOKEN)
curl -X GET http://localhost:8080/api/protected/user \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## Postman Collection

Você pode importar esta coleção no Postman:

```json
{
  "info": {
    "name": "Quarkus Auth API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Register",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"username\": \"joao\",\n  \"email\": \"joao@example.com\",\n  \"password\": \"senha123\"\n}"
        },
        "url": {
          "raw": "http://localhost:8080/api/auth/register",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "auth", "register"]
        }
      }
    },
    {
      "name": "Login",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"username\": \"joao\",\n  \"password\": \"senha123\"\n}"
        },
        "url": {
          "raw": "http://localhost:8080/api/auth/login",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "auth", "login"]
        }
      }
    },
    {
      "name": "Get User Info",
      "request": {
        "method": "GET",
        "header": [
          {
            "key": "Authorization",
            "value": "Bearer {{token}}"
          }
        ],
        "url": {
          "raw": "http://localhost:8080/api/protected/user",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "protected", "user"]
        }
      }
    }
  ]
}
```
