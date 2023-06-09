package com.github.polyrocketmatt.nexus.common.client;

import com.github.polyrocketmatt.nexus.common.entity.NexusPlayer;
import com.github.polyrocketmatt.nexus.common.exception.NexusClientException;
import com.github.polyrocketmatt.nexus.common.utils.NexusLogger;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class NexusPlayerStatusClient extends NexusClient {

    private final NexusPlayer player;
    private final Gson gson = new Gson();

    public NexusPlayerStatusClient(NexusPlayer player) {
        this.player = player;
    }

    @Override
    public <T> @NotNull T get(@NotNull Class<T> clazz) {
        String uuid = player.getUniqueId().toString();
        String ip = player.getIpAddress();

        try {
            HttpRequest request = construct(Endpoint.USER, Method.GET, "", uuid, ip).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            NexusLogger.inform("Sending GET request to %s", NexusLogger.LogType.COMMON,  request.uri().toString());
            NexusLogger.inform("Response: %s", NexusLogger.LogType.COMMON, response.body());

            return gson.fromJson(response.body(), clazz);
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
