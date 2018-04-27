package com.tsunderebug.discordintellij;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;


public class PresenceConfigurable implements Configurable {
    private PresenceConfigurableGUI presenceGUI;

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
        presenceGUI = new PresenceConfigurableGUI();
        return presenceGUI.getApplicationPresence();
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}
