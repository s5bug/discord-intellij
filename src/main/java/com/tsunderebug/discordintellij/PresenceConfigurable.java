package com.tsunderebug.discordintellij;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class PresenceConfigurable implements Configurable {
    private PresenceConfigurableGUI gui;

    @Nls
    @Override
    public String getDisplayName() {
        return "Presence";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "preference.PresenceConfigurable";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        gui = new PresenceConfigurableGUI();
        return gui.getTopLevel();
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}
