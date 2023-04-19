package com.github.polyrocketmatt.nexus.common.client;

import com.github.polyrocketmatt.nexus.common.exception.NexusClientException;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class NexusModClient extends NexusClient {

    private final Gson gson = new Gson();

    public NexusModClient() {
    }

    @Override
    public <T> @NotNull T get(@NotNull Class<T> clazz) {
        try {
            HttpRequest request = construct(Endpoint.FORGE, Method.GET, "").build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            NexusLogger.inform("Sending GET request to %s", NexusLogger.LogType.COMMON,  request.uri().toString());

            return gson.fromJson(response.body(), new TypeToken<Map<String, String>>(){}.getType());
        } catch (IOException ex) {
            throw new NexusClientException("Failed to send GET request (IOException occurred)", url, Method.GET);
        } catch (InterruptedException ex) {
            throw new NexusClientException("Failed to send GET request (Interrupted)", url, Method.GET);
        }
    }

    @Override
    public <T, K> @NotNull T post(@NotNull K body, @NotNull Class<T> clazz) {
        throw new NexusClientException("Unsupported Client Method", url, Method.POST);
    }

    @Override
    public <T, K> @NotNull T put(@NotNull K body, @NotNull Class<T> clazz) {
        throw new NexusClientException("Unsupported Client Method", url, Method.PUT);
    }

    @Override
    public <T> @NotNull T delete(@NotNull Class<T> clazz) {
        throw new NexusClientException("Unsupported Client Method", url, Method.DELETE);
    }

}
