package io.getstream.chat.java.services.framework;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public final class UserClient implements Client {

    private final Client delegate;
    private final UserToken userToken;

    public UserClient(Client delegate, String userToken) {
        this.delegate = delegate;
        this.userToken = new UserToken(userToken);
    }

    @Override
    public <TService> @NotNull TService create(Class<TService> svcClass) {
        return delegate.create(svcClass, userToken);
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
}
