package com.tsunderebug.discordintellij.Slack;

import com.intellij.openapi.project.Project;
import com.tsunderebug.discordintellij.PresenceAgent;
import com.tsunderebug.discordintellij.TogglePresence;

public class SlackTogglePresence extends TogglePresence {

    @Override
    public PresenceAgent getAgent(Project project) {
        return project.getComponent(SlackAgent.class, new SlackAgent());
    }

}
