package com.tsunderebug.discordintellij;

import com.intellij.openapi.components.ApplicationComponent;

public class StartupShutdown implements ApplicationComponent {

	@Override
	public void initComponent() {
		DiscordIntelliJ.enableRPC();
	}

	@Override
	public void disposeComponent() {
		DiscordIntelliJ.stopRPC();
	}

}
