package com.tsunderebug.discordintellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.util.IconLoader;

import javax.swing.Icon;

public class ToggleAction extends AnAction {

	public static final Icon ENABLED = IconLoader.findIcon("/icons/enabled.png");
	public static final Icon DISABLED = IconLoader.findIcon("/icons/disabled.png");

	@Override
	public void actionPerformed(AnActionEvent a) {
		PresenceEnabled pe = a.getProject().getComponent(PresenceEnabled.class, new PresenceEnabled());
		if(pe.isEnabled()) {
			pe.loadState(new PresenceEnabled(false));
			a.getPresentation().setIcon(ToggleAction.DISABLED);
			DiscordIntelliJ.hideRPC();
		} else {
			pe.loadState(new PresenceEnabled());
			a.getPresentation().setIcon(ToggleAction.ENABLED);
			DiscordIntelliJ.enableRPC(a.getProject());
		}
	}

}
