package com.github.polyrocketmatt.nexus.common.modules;

import com.github.polyrocketmatt.nexus.api.entity.NexusEntity;
import com.github.polyrocketmatt.nexus.api.events.NexusEvent;
import com.github.polyrocketmatt.nexus.api.modules.ModuleProcessResult;
import com.github.polyrocketmatt.nexus.api.modules.NexusModule;
import com.github.polyrocketmatt.nexus.api.modules.NexusModuleType;
import com.github.polyrocketmatt.nexus.common.Nexus;
import com.github.polyrocketmatt.nexus.common.entity.NexusPlayer;
import com.github.polyrocketmatt.nexus.common.entity.NexusPlayerConnection;
import com.github.polyrocketmatt.nexus.common.event.PlayerConnectEvent;
import com.github.polyrocketmatt.nexus.common.exception.NexusException;
import com.github.polyrocketmatt.nexus.common.modules.result.ConnectionDetectionResult;
import com.github.polyrocketmatt.nexus.common.modules.result.PlainProcessResult;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class ClientConnectionModule extends NexusModule {

    private final double torScore = Double.parseDouble(Nexus.getProperties().getProperty("client-connection.weights.tor"));
    private final double proxyScore = Double.parseDouble(Nexus.getProperties().getProperty("client-connection.weights.proxy"));
    private final double datacenterScore = Double.parseDouble(Nexus.getProperties().getProperty("client-connection.weights.dc"));
    private final double anonymousScore = Double.parseDouble(Nexus.getProperties().getProperty("client-connection.weights.anon"));
    private final double attackScore = Double.parseDouble(Nexus.getProperties().getProperty("client-connection.weights.attacker"));
    private final double abuserScore = Double.parseDouble(Nexus.getProperties().getProperty("client-connection.weights.abuser"));
    private final double threatScore = Double.parseDouble(Nexus.getProperties().getProperty("client-connection.weights.threat"));
    private final double thresholdScore = Double.parseDouble(Nexus.getProperties().getProperty("client-connection.weights.threshold"));



    @Override
    public @NotNull NexusModuleType getModuleType() {
        return NexusModuleType.CLIENT_CONNECTION;
    }

    @Override
    public @NotNull ModuleProcessResult process(@NotNull NexusEntity nexusEntity, @NotNull NexusEvent nexusEvent) {
        if (!(nexusEvent instanceof PlayerConnectEvent event))
            return new PlainProcessResult(nexusEntity, false);
        if (!(nexusEntity instanceof NexusPlayer player))
            return new PlainProcessResult(nexusEntity, false);
        if (event.isLocal())
            return new PlainProcessResult(nexusEntity, false);

        //  Find the current connection
        NexusPlayerConnection connection = player.getPlayerData()
                .connections()
                .stream()
                .filter(con -> con.ip().equals(event.getAddress().getHostAddress()))
                .findFirst()
                .orElse(null);

        if (connection == null)
            throw new NexusException("Unable to find connection for player %s".formatted(player.getUniqueId()));

        //  Compute score
        Set<ConnectionDetectionResult.ConnectionType> types = new HashSet<>();
        double score = 0.0;
        if (connection.tor())       { score += torScore; types.add(ConnectionDetectionResult.ConnectionType.TOR); }
        if (connection.prox())      { score += proxyScore; types.add(ConnectionDetectionResult.ConnectionType.PROXY); }
        if (connection.dc())        { score += datacenterScore; types.add(ConnectionDetectionResult.ConnectionType.DATACENTER); }
        if (connection.anon())      { score += anonymousScore; types.add(ConnectionDetectionResult.ConnectionType.ANONYMOUS); }
        if (connection.atk())       { score += attackScore; types.add(ConnectionDetectionResult.ConnectionType.ATTACK); }
        if (connection.abr())       { score += abuserScore; types.add(ConnectionDetectionResult.ConnectionType.ABUSER); }
        if (connection.thr())       { score += threatScore; types.add(ConnectionDetectionResult.ConnectionType.THREAT); }

        //  Check if the score is above the threshold
        boolean failed = score >= thresholdScore;

        return new ConnectionDetectionResult(player, failed, types);
    }
}
