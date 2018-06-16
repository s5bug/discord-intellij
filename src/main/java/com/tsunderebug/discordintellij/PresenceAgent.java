package com.tsunderebug.discordintellij;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;

import java.util.ArrayList;

public abstract class PresenceAgent implements PresenceChangeListener {
    private static ArrayList<Class<? extends PresenceAgent>> agentClasses = new ArrayList<>();

    private boolean initialized = false;
    private final TogglePresence togglePresence;
    private Project currentProject = null;

    protected PresenceAgent(TogglePresence togglePresence) {
        Presence.registerAgent(this);
        this.togglePresence = togglePresence;
    }

    public static ArrayList<Class<? extends PresenceAgent>> getAgentClasses() {
        return agentClasses;
    }

    protected static void addAgent(Class<? extends PresenceAgent> derivedClass) {
        if (!agentClasses.contains(derivedClass)) {
            agentClasses.add(derivedClass);
        }
    }

    protected static boolean setEnabled(boolean enabled, Class<? extends PresenceAgent> agentClass) {
        if (enabled) {
            return AgentManager.addAgent(agentClass);
        } else {
            return AgentManager.removeAgent(agentClass);
        }
    }

    public abstract void initializeAgent();
    public abstract String getName();

    public abstract void showPresence();

    public abstract void hidePresence();

    public abstract void stopAgent();

    public void init() {
        if (isActive()) {
            if (initialized) {
                Notifications.Bus.notify(
                        new Notification(
                                "presence",
                                "Presence Agent Reinitialization",
                                "An attempt has been made to initialize " +
                                        this.getName() + " which has previously been initialized.",
                                NotificationType.INFORMATION));
            }
            initializeAgent();
            initialized = true;
        }
    }

    private boolean isActive() {
        if (getCurrentProject() == null) {
            return true;
        }
        return togglePresence.getActive(getCurrentProject()).isActive();
    }

    public void show() {
        if (isActive() && initialized) {
            showPresence();
        }
    }

    public void hide() {
        if (isActive() && initialized) {
            hidePresence();
        }
    }

    public void stop() {
        if (isActive() && initialized) {
            initialized = false;
            stopAgent();
        }
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }
}
