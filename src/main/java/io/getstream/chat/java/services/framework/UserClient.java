package io.getstream.chat.java.services.framework;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;

/**
 * Client implementation for user-scoped API operations.
 * <p>
 * This client wraps a base {@link Client} and automatically injects a user-specific
 * authentication token into all service calls. It's designed for scenarios where
 * different users need to make authenticated API calls without creating separate
 * client instances per user.
 * </p>
 *
 * @see Client
 */
public final class UserClient implements Client {

    private final Client delegate;
    private final String userToken;

    /**
     * Constructs a new UserClient that wraps the provided client with user authentication.
     *
     * @param delegate the base client to delegate calls to
     * @param userToken the user-specific authentication token to inject into requests
     */
    public UserClient(Client delegate, String userToken) {
        this.delegate = delegate;
        this.userToken = userToken;
    }

    /**
     * Creates a service proxy that automatically injects the user token into all requests.
     *
     * @param svcClass the service interface class
     * @param <TService> the service type
     * @return a proxy instance of the service with user token injection
     */
    @Override
    public <TService> @NotNull TService create(Class<TService> svcClass) {
        return delegate.create(svcClass, userToken);
    }

    /**
     * Returns the API key from the underlying client.
     *
     * @return the API key
     */
    @Override
    public @NotNull String getApiKey() {
        return delegate.getApiKey();
    }

    /**
     * Returns the API secret from the underlying client.
     *
     * @return the API secret
     */
    @Override
    public @NotNull String getApiSecret() {
        return delegate.getApiSecret();
    }

    /**
     * Sets the request timeout duration on the underlying client.
     *
     * @param timeoutDuration the timeout duration to set
     */
    @Override
    public void setTimeout(@NotNull Duration timeoutDuration) {
        delegate.setTimeout(timeoutDuration);
    }
}
