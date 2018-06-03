package com.tsunderebug.discordintellij.slack;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.tsunderebug.discordintellij.PresenceActive;
import com.tsunderebug.discordintellij.PresenceAgent;
import com.tsunderebug.discordintellij.TogglePresence;

public class SlackTogglePresence extends TogglePresence {

    @Override
    public PresenceActive getActive(Project project) {
        return ServiceManager.getService(project, SlackActive.class).getPresenceActive();
    }

    @Override
    public Class<? extends PresenceAgent> getAgentClass() {
        return SlackAgent.class;
    }
}
