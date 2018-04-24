package com.tsunderebug.discordintellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public class TogglePresence extends AnAction {

	private static final Icon ENABLED = IconLoader.findIcon("/icons/enabled.png");
	private static final Icon DISABLED = IconLoader.findIcon("/icons/disabled.png");

	@Override
	public void actionPerformed(AnActionEvent actionEvent) {
		Project project = actionEvent.getProject();
		if (project == null) {
			return;
		}

		PresenceEnabled presenceEnabled = project.getComponent(PresenceEnabled.class, new PresenceEnabled());
		if(presenceEnabled.isEnabled()) {
			presenceEnabled.loadState(new PresenceEnabled(false));
			actionEvent.getPresentation().setIcon(TogglePresence.DISABLED);
			AgentManager.getAgents().forEach(PresenceAgent::hide);
		} else {
			presenceEnabled.loadState(new PresenceEnabled(true));
			actionEvent.getPresentation().setIcon(TogglePresence.ENABLED);
			AgentManager.getAgents().forEach(x -> x.enable(Presence.getInstance()));
		}
	}

}
