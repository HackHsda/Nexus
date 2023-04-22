package com.github.polyrocketmatt.nexus.paper.handlers;

import com.github.polyrocketmatt.nexus.api.PlatformType;
import com.github.polyrocketmatt.nexus.api.entity.NexusEntity;
import com.github.polyrocketmatt.nexus.api.events.NexusEvent;
import com.github.polyrocketmatt.nexus.api.module.NexusModuleType;
import com.github.polyrocketmatt.nexus.common.entity.NexusPlayer;
import com.github.polyrocketmatt.nexus.common.entity.NexusPlayerConnection;
import com.github.polyrocketmatt.nexus.common.event.PlayerConnectEvent;
import com.github.polyrocketmatt.nexus.common.exception.NexusException;
import com.github.polyrocketmatt.nexus.common.modules.ModuleHandler;
import com.github.polyrocketmatt.nexus.paper.PaperNexus;

public class PaperConnectionHandler extends ModuleHandler {

    private final String ipDetectedMessage = PaperNexus.getInstance().getMessages().getString("ip-detected");
    private final double torScore = PaperNexus.getInstance().getEnvDouble("client-connection.weights.tor");
    private final double proxyScore = PaperNexus.getInstance().getEnvDouble("client-connection.weights.proxy");
    private final double datacenterScore = PaperNexus.getInstance().getEnvDouble("client-connection.weights.dc");
    private final double anonymousScore = PaperNexus.getInstance().getEnvDouble("client-connection.weights.anon");
    private final double attackScore = PaperNexus.getInstance().getEnvDouble("client-connection.weights.attacker");
    private final double abuserScore = PaperNexus.getInstance().getEnvDouble("client-connection.weights.abuser");
    private final double threatScore = PaperNexus.getInstance().getEnvDouble("client-connection.weights.threat");
    private final double thresholdScore = PaperNexus.getInstance().getEnvDouble("client-connection.weights.threshold");

    public PaperConnectionHandler() {
        super(NexusModuleType.CLIENT_CONNECTION);
    }

    @Override
    public PlatformType getPlatformType() {
        return PlatformType.PAPER;
    }

    @Override
    public void process(NexusEvent nexusEvent, NexusEntity entity) {
        if (!(nexusEvent instanceof PlayerConnectEvent event))
            return;
        if (!(entity instanceof NexusPlayer player))
            return;
        if (event.isLocal())
            return;

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
        double score = 0.0;
        if (connection.tor())       score += torScore;
        if (connection.prox())      score += proxyScore;
        if (connection.dc())        score += datacenterScore;
        if (connection.anon())      score += anonymousScore;
        if (connection.atk())       score += attackScore;
        if (connection.abr())       score += abuserScore;
        if (connection.thr())       score += threatScore;
        if (score > thresholdScore)
            player.sendMessage(ipDetectedMessage.formatted(event.getAddress().getHostAddress()));
    }
}
