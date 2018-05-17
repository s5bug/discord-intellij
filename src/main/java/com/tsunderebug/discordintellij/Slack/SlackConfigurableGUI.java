package com.tsunderebug.discordintellij.Slack;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.components.JBList;
import com.tsunderebug.discordintellij.AgentManager;
import com.tsunderebug.discordintellij.PresenceAgent;

import javax.swing.*;
import java.util.Optional;

public class SlackConfigurableGUI {
    private JPanel slackPresence;
    private JBList<SlackTeamMember> teamList;
    private JButton addTeamButton;
    private JButton removeTeamButton;
    @SuppressWarnings("unused")
    private JTabbedPane teamPane;
    private DefaultListModel<SlackTeamMember> teams;

    SlackConfigurableGUI() {

        addTeamButton.addActionListener(e -> {
            SlackAgent slackAgent;
            Optional<? extends PresenceAgent> agent = AgentManager.getAgent(SlackAgent.class);

            if (!agent.isPresent() || !SlackAgent.class.isInstance(agent.get())) {
                AgentManager.addAgent(SlackAgent.class);
                agent = AgentManager.getAgent(SlackAgent.class);
            }
            if (agent.isPresent()) {
                slackAgent = (SlackAgent) agent.get();
            } else {
                Notifications.Bus.notify(
                        new Notification(
                                "presence",
                                "Unable to find presence agent for Slack.",
                                "Slack teams can not be added.",
                                NotificationType.ERROR));
                return;
            }

            ApplicationManager.getApplication().invokeLater(
                    () -> {
                        Optional<SlackTeamMember> team = slackAgent.addTeam();
                        team.ifPresent(teams::addElement);
                        SlackAgent.setEnabled(true);
                    });

        });

        removeTeamButton.addActionListener(e -> {
            Optional<? extends PresenceAgent> agent = AgentManager.getAgent(SlackAgent.class);
            SlackAgent.setEnabled(teams.size() > 1);
            agent.ifPresent(presenceAgent -> ((SlackAgent) presenceAgent).removeTeam(teamList.getSelectedValue()));
            teams.removeElementAt(teamList.getSelectedIndex());
        });
    }

    public JComponent getTopLevel() {
        return slackPresence;
    }

    private void createUIComponents() {
        teams = new DefaultListModel<>();
        SlackAgent.getTeams().forEach(teams::addElement);
        teamList = new JBList<>(teams);
        teamList.setCellRenderer(new SlackTeamCellRenderer());
        teamList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            removeTeamButton.setEnabled(!teamList.isSelectionEmpty());
        });
    }
}
