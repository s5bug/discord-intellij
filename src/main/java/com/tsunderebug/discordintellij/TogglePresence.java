package com.tsunderebug.discordintellij;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

import java.util.Optional;

public abstract class TogglePresence extends ToggleAction {
    private final static Logger LOG = Logger.getInstance(ProjectStartStop.class);
    private static final String DISABLE_TEXT = "Disable";
    private static final String ENABLE_TEXT = "Enable";

    public abstract PresenceActive getActive(Project project);

    public abstract Class<? extends PresenceAgent> getAgentClass();

    @Override
    public boolean isSelected(AnActionEvent actionEvent) {
        Project project = actionEvent.getProject();
        if (project == null) {
            return false;
        }

        PresenceActive presece = getActive(project);
        return presece.isActive();
    }

    @Override
    public void setSelected(AnActionEvent actionEvent, boolean state) {
        Project project = actionEvent.getProject();
        if (project == null) {
            return;
        }

        Optional<? extends PresenceAgent> agent = AgentManager.getAgent(getAgentClass());
        PresenceActive presence = getActive(project);
        if (!state && agent.isPresent()) {
            agent.get().hide();
        }
        presence.setActive(state);
        if (agent.isPresent()) {
            actionEvent.getPresentation().setText(String.format("%s %s", presence.isActive() ? DISABLE_TEXT : ENABLE_TEXT, agent.get().getName()));
            agent.get().update();
        }
    }

}
