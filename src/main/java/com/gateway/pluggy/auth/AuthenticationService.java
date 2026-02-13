package com.gateway.pluggy.auth;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.SetArgs;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AuthenticationService {
  private static final Logger LOG = Logger.getLogger(AuthenticationService.class);
  private static final String CACHE_KEY = "pluggy:auth:token";

  @ConfigProperty(name = "pluggy.api.url")
  String apiUrl;

  @ConfigProperty(name = "pluggy.auth.cache.ttl", defaultValue = "3600")
  Long cacheTtlSeconds;

  private final ValueCommands<String, String> tokenCache;
  private final HttpClient httpClient;
  private final ObjectMapper objectMapper;

  @ConfigProperty(name = "pluggy.client.id")

  String clientId;
  @ConfigProperty(name = "pluggy.client.secret")
  String clientSecret;

  public AuthenticationService(RedisDataSource redisDataSource) {
    this.tokenCache = redisDataSource.value(String.class);
    this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    this.objectMapper = new ObjectMapper();
  }

  public String getAuthToken() {
    String cachedToken = tokenCache.get(CACHE_KEY);
    if (cachedToken != null) {
      LOG.debug("Token encontrado no cache");
      return cachedToken;
    }

    LOG.debug("Token não encontrado, autenticando...");
    return authenticate();
  }

  private String authenticate() {
    try {
      Map<String, String> payload = Map.of(
          "clientId", clientId,
          "clientSecret", clientSecret);
      String json = objectMapper.writeValueAsString(payload);

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(apiUrl + "/auth"))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(json))
          .build();

      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() != 200) {
        LOG.errorf("Authentication failed: %d - %s", response.statusCode(), response.body());
        LOG.errorf("payload: %s", json);
        LOG.errorf("payloadstring: %s", HttpRequest.BodyPublishers.ofString(json));
        throw new RuntimeException("Authentication failed: " + response.statusCode());
      }

      @SuppressWarnings("unchecked")
      Map<String, String> body = objectMapper.readValue(response.body(), Map.class);
      String token = body.get("apiKey");

      SetArgs setArgs = new SetArgs().ex(cacheTtlSeconds);
      tokenCache.set(CACHE_KEY, token, setArgs);
      LOG.infof("Token cacheado com sucesso (TTL: %ds)", cacheTtlSeconds);

      return token;
    } catch (Exception e) {
      LOG.error("Falha na autenticação", e);
      throw new RuntimeException("Authentication failed", e);
    }
  }
}
