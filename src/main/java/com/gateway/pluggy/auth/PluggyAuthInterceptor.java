package com.gateway.pluggy.auth;

import org.jboss.logging.Logger;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@RequiresPluggyAuth
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class PluggyAuthInterceptor {
  private static final Logger LOG = Logger.getLogger(PluggyAuthInterceptor.class);

  @Inject
  AuthenticationService authService;

  @Inject
  PluggyAuthContext authContext;

  @AroundInvoke
  public Object authenticateBeforeInvocation(InvocationContext context) throws Exception {
    LOG.debugf("Autenticando antes de: %s", context.getMethod().getName());

    try {
      String token = authService.getAuthToken();
      authContext.setToken(token);
      return context.proceed();
    } finally {
      authContext.clear();
    }
  }
}
