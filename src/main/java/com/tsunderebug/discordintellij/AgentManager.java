package com.tsunderebug.discordintellij;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class AgentManager {
    private static Map<Class<? extends PresenceAgent>, PresenceAgent> agents = new HashMap<>();
    private static Supplier<Stream<PresenceAgent>> agentSupplier = () -> agents.values().stream();

    public static void initializeAgents() {
        for (Class<? extends PresenceAgent> agent : PresenceAgent.getAgentClasses()) {
            Method method = null;
            try {
                method = agent.getMethod("isEnabled");
                if ((boolean) method.invoke(null)) {
                    agents.put(agent, agent.newInstance());
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                Notifications.Bus.notify(
                        new Notification(
                                "presence",
                                "Unable to create presence agent " + agent.getName() + ".",
                                e.toString(),
                                NotificationType.ERROR));
            }
        }
    }

    public static Stream<PresenceAgent> getAgents() {
        return agentSupplier.get();
    }

    public static Optional<? extends PresenceAgent> getAgent(Class<? extends PresenceAgent> agentClass) {
        return Optional.ofNullable(agents.get(agentClass));
    }

    public static boolean addAgent(Class<? extends PresenceAgent> agentClass) {
        PresenceAgent agent;
        try {
            agent = agentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            Notifications.Bus.notify(
                    new Notification(
                            "presence",
                            "Unable to add presence agent " + agentClass.getName() + ".",
                            e.getMessage(),
                            NotificationType.ERROR));
            return false;
        }
        agents.put(agentClass, agent);
        return true;
    }

    public static boolean removeAgent(Class<? extends PresenceAgent> agentClass) {
        return (agents.remove(agentClass) != null);
    }
}
