package com.tsunderebug.discordintellij;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.diagnostic.Logger;
import com.tsunderebug.discordintellij.Slack.SlackAgent;

import java.util.ArrayList;

public abstract class PresenceAgent {
    private final static Logger LOG = Logger.getInstance(SlackAgent.class);
    private static ArrayList<Class<? extends PresenceAgent>> agentClasses = new ArrayList<>();

    private boolean active = true;
    private boolean initialized = false;

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

    public abstract void showPresence(Presence presence);

    public abstract void hidePresence();

    public abstract void stopAgent();

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void togglePresence() {
        active = !active;
    }

    public void init() {
        if (active) {
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

    public void show(Presence presence) {
        if (active && initialized) {
            showPresence(presence);
        }
    }

    public void hide() {
        if (active && initialized) {
            hidePresence();
        }
    }

    public void stop() {
        if (active && initialized) {
            initialized = false;
            stopAgent();
        }
    }
    public void update(Presence presence) {
        if (active) {
            showPresence(presence);
        } else {
            hidePresence();
        }
    }
}
