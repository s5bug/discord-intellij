package com.tsunderebug.discordintellij.Discord;

import com.intellij.openapi.project.Project;
import com.tsunderebug.discordintellij.PresenceAgent;
import com.tsunderebug.discordintellij.TogglePresence;

public class DiscordTogglePresence extends TogglePresence {
    @Override
    public PresenceAgent getAgent(Project project) {
        return project.getComponent(DiscordAgent.class, new DiscordAgent());
    }
}
