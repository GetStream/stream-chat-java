package io.getstream.chat.java.services.framework;

import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.Request;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.time.Duration;

public final class UserClient implements Client {

    private final Client delegate;
    private final String userToken;

    public UserClient(Client delegate, String userToken) {
        this.delegate = delegate;
        this.userToken = userToken;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TService> @NotNull TService create(Class<TService> svcClass) {
        TService service = delegate.create(svcClass);
        
        return (TService) Proxy.newProxyInstance(
            svcClass.getClassLoader(),
            new Class<?>[]{svcClass},
            (proxy, method, args) -> {
                Object result = method.invoke(service, args);
                
                if (result instanceof Call) {
                    return taggedCall((Call<?>) result);
                }
                return result;
            }
        );
    }

    private <T> Call<T> taggedCall(Call<T> original) {
        return new Call<T>() {
            @Override
            public Request request() {
                return original.request().newBuilder()
                    .tag(UserToken.class, new UserToken(userToken))
                    .build();
            }

            @Override
            public Response<T> execute() throws IOException {
                return original.execute();
            }

            @Override
            public void enqueue(Callback<T> callback) {
                original.enqueue(callback);
            }

            @Override
            public boolean isExecuted() {
                return original.isExecuted();
            }

            @Override
            public void cancel() {
                original.cancel();
            }

            @Override
            public boolean isCanceled() {
                return original.isCanceled();
            }

            @Override
            public Call<T> clone() {
                return taggedCall(original.clone());
            }

            @Override
            public okio.Timeout timeout() {
                return original.timeout();
            }
        };
    }

    @Override
    public @NotNull String getApiKey() {
        return delegate.getApiKey();
    }

    @Override
    public @NotNull String getApiSecret() {
        return delegate.getApiSecret();
    }

    @Override
    public void setTimeout(@NotNull Duration timeoutDuration) {
        delegate.setTimeout(timeoutDuration);
    }

    public static class UserToken {
        public final String token;
        
        public UserToken(String token) {
            this.token = token;
        }
    }
}
