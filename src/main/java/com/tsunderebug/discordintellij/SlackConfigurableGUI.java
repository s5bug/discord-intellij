package com.tsunderebug.discordintellij;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SlackConfigurableGUI {
    private JPanel slackPresence;
    private JCheckBox slackPresenceEnabled;
    private JButton addToSlackButton;

    public SlackConfigurableGUI() {
        addToSlackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = "https://slack.com/oauth/authorize?client_id=7071277312.352145492401&scope=users.profile:write";
            }
        });
    }

    public JComponent getTopLevel() {
        return slackPresence;
    }
}
