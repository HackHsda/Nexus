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
import java.time.Duration;
import java.util.UUID;

public abstract class NexusClient {

    public enum Endpoint {
        USER("user"),
        FORGE("forge");

        private final String endpoint;

        Endpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getEndpoint() {
            return endpoint;
        }
    }

    public enum Method {
        GET,
        POST,
        PUT,
        DELETE
    }

    protected final String url;
    protected final HttpClient client;

    public NexusClient() {
        this.url = "https://nexus-vercel-theta.vercel.app/gateway"; //Nexus.getProperties().getProperty("network.rest.url");
        this.client = HttpClient.newHttpClient();
    }

    protected @NotNull HttpRequest.Builder construct(@NotNull Endpoint endpoint, @NotNull Method method,
                                                     @Nullable String body, @NotNull String... params) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(30));

            //  Construct the URI
            String operation = switch (method) {
                case GET -> "get";
                case POST -> "post";
                case PUT -> "put";
                case DELETE -> "delete";
            };

            StringBuilder uri = new StringBuilder("%s/%s/%s".formatted(url, endpoint.endpoint, operation));
            if (params.length == 0)
                uri.append("/");
            else
                for (String param : params)
                    uri.append("/%s".formatted(param));

            switch (method) {
                case GET        -> builder.GET();
                case POST       -> builder.POST(bodyPublisher(body));
                case PUT        -> builder.PUT(bodyPublisher(body));
                case DELETE     -> builder.DELETE();
            }

            return builder.uri(new URI(uri.toString()));
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
