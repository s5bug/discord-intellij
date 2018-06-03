package com.tsunderebug.discordintellij.discord;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class DiscordPresenceConfigurable implements Configurable {
    private DiscordConfigurableGUI presenceGUI;

    @Nls
    @Override
    public String getDisplayName() {
        return "Presence for IntelliJ";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "preference.PresenceConfigurable";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        presenceGUI = new DiscordConfigurableGUI();
        return presenceGUI.getTopLevel();
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}
