package com.tsunderebug.discordintellij;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;


public class PresenceConfigurableSlack implements Configurable {
    private SlackConfigurableGUI presenceGUI;

    @Nls
    @Override
    public String getDisplayName() {
        return "Slack";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "preference.PresenceConfigurableSlack";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        presenceGUI = new SlackConfigurableGUI();
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
