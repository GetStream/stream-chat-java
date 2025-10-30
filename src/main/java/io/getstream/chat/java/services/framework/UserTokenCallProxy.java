package io.getstream.chat.java.services.framework;

import okhttp3.Call;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Dynamic proxy that intercepts Retrofit service calls and injects UserToken
 * into the request by modifying the internal rawCall field via reflection.
 * 
 * This approach allows per-call authentication without creating multiple OkHttpClient
 * instances, making it suitable for multi-tenant systems with thousands of users.
 */
class UserTokenCallProxy implements InvocationHandler {
  private static volatile Field rawCallField;
  
  private final Call.Factory callFactory;
  private final Object delegate;
  private final UserToken token;

  UserTokenCallProxy(@NotNull Call.Factory callFactory, @NotNull Object delegate, @NotNull UserToken token) {
    this.callFactory = callFactory;
    this.delegate = delegate;
    this.token = token;
  }
  
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Object result = method.invoke(delegate, args);
    
    // If the result is a Retrofit Call, inject the user token
    if (result instanceof retrofit2.Call) {
      return injectTokenIntoCall((retrofit2.Call<?>) result);
    }
    
    return result;
  }
  
  private retrofit2.Call<?> injectTokenIntoCall(retrofit2.Call<?> originalCall) {
    retrofit2.Call<?> clonedCall = originalCall.clone();
    
    try {
      // Cache field lookup for performance (double-checked locking)
      if (rawCallField == null) {
        synchronized (UserTokenCallProxy.class) {
          if (rawCallField == null) {
            rawCallField = clonedCall.getClass().getDeclaredField("rawCall");
            rawCallField.setAccessible(true);
          }
        }
      }
      
      // Create new request with token tag
      Request newRequest = originalCall.request().newBuilder()
        .tag(UserToken.class, token)
        .build();
      
      // Create new OkHttp call with modified request
      okhttp3.Call newOkHttpCall = callFactory.newCall(newRequest);
      
      // Inject the new call into the cloned Retrofit call
      rawCallField.set(clonedCall, newOkHttpCall);
      
      return clonedCall;
    } catch (NoSuchFieldException e) {
      // If Retrofit's internal structure changes, provide clear error message
      throw new RuntimeException(
        "Retrofit internal structure changed. Field 'rawCall' not found in " + 
        clonedCall.getClass().getName() + ". Update client implementation.", e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Failed to inject token into call", e);
    }
  }
}

