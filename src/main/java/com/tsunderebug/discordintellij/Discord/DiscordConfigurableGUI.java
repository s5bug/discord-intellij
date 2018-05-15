package com.tsunderebug.discordintellij.Discord;

import com.intellij.ide.util.PropertiesComponent;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static com.tsunderebug.discordintellij.Discord.DiscordAgent.DISCORD_PRESENCE_ENABLED;

public class DiscordConfigurableGUI implements ItemListener {
    private JPanel discordEnabled;
    private JCheckBox discordEnabledCheckbox;

    public JComponent getTopLevel() {
        return discordEnabled;
    }

    private void createUIComponents() {
        discordEnabledCheckbox = new JCheckBox("Discord Enabled", null,
                PropertiesComponent.getInstance().getBoolean(DISCORD_PRESENCE_ENABLED, true));
        discordEnabledCheckbox.addItemListener(this);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        PropertiesComponent.getInstance().setValue(
                DISCORD_PRESENCE_ENABLED,
                String.valueOf(discordEnabledCheckbox.isSelected()),
                "true");
        DiscordAgent.setEnabled(discordEnabledCheckbox.isSelected());
    }
}
