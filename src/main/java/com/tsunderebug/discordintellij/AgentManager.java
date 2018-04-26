package com.tsunderebug.discordintellij;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class AgentManager {
    private static Supplier<Stream<PresenceAgent>> agents = () -> Arrays.stream(new PresenceAgent[]{
            new DiscordAgent(),
            new SlackAgent()});

    public static Stream<PresenceAgent> getAgents() {
        return agents.get();
    }
}
