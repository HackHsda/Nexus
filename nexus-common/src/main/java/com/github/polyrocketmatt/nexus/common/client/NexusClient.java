package com.github.polyrocketmatt.nexus.common.client;

import com.github.polyrocketmatt.nexus.common.Nexus;
import com.github.polyrocketmatt.nexus.common.exception.NexusClientException;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.UUID;

public abstract class NexusClient {

    public enum Method {
        GET,
        POST,
        PUT,
        DELETE
    }

    protected final String url;
    protected final HttpClient client;

    public NexusClient() {
        this.url = Nexus.getProperties().getProperty("network.rest.url");
        this.client = HttpClient.newHttpClient();

        NexusLogger.inform("A new client has been configured with the following parameters:", NexusLogger.LogType.COMMON);
        NexusLogger.inform("    URL: %s".formatted(this.url), NexusLogger.LogType.COMMON);
    }

    protected @NotNull HttpRequest construct(@NotNull Method method, @NotNull UUID uuid, @Nullable String body) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", uuid.toString());

            switch (method) {
                case GET        -> builder.GET();
                case POST       -> builder.POST(bodyPublisher(body));
                case PUT        -> builder.PUT(bodyPublisher(body));
                case DELETE     -> builder.DELETE();
            }

            return builder.build();
        } catch (URISyntaxException ex) {
            throw new NexusClientException("Failed to construct GET request", url, Method.GET);
        }
    }

    private HttpRequest.BodyPublisher bodyPublisher(@Nullable String body) {
        return (body == null) ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body);
    }

    public abstract <T> @NotNull T get(@NotNull Class<T> clazz);

    public abstract <T, K> @NotNull T post(@NotNull K body, @NotNull Class<T> clazz);

    public abstract <T, K> @NotNull T put(@NotNull K body, @NotNull Class<T> clazz);

    public abstract <T> @NotNull T delete(@NotNull Class<T> clazz);

}
