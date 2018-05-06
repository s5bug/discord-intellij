package com.tsunderebug.discordintellij;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

public class TogglePresence extends ToggleAction {
    private final static Logger LOG = Logger.getInstance(ProjectStartStop.class);
    private static final String DISABLE_TEXT = "Disable";
    private static final String ENABLE_TEXT = "Enable";


    @Override
    public boolean isSelected(AnActionEvent actionEvent) {
        Project project = actionEvent.getProject();
        if (project == null) {
            return false;
        }

        PresenceAgent agent = getAgent(project);
        return agent.isActive();
    }

    @Override
    public void setSelected(AnActionEvent actionEvent, boolean state) {
        Project project = actionEvent.getProject();
        if (project == null) {
            return;
        }

        PresenceAgent agent = getAgent(project);
        agent.setActive(state);
        actionEvent.getPresentation().setText(String.format("%s %s", agent.isActive() ? DISABLE_TEXT : ENABLE_TEXT, agent.getName()));
        agent.update(Presence.getInstance());

    }

    public PresenceAgent getAgent(Project project) {
        LOG.error("It is invalid to call TogglePresence directly, you must implement a subclass.");
        throw new UnsupportedOperationException();
    }
}
