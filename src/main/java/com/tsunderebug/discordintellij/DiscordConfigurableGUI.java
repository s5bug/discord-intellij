package com.tsunderebug.discordintellij;

import javax.swing.*;

public class DiscordConfigurableGUI {
    private JPanel discordEnabled;
    private JCheckBox discordEnabledCheckbox;

    public JComponent getTopLevel() {
        return discordEnabled;
    }
}
