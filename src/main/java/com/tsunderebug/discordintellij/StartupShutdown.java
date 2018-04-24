package com.tsunderebug.discordintellij;

import com.intellij.openapi.components.ApplicationComponent;

public class StartupShutdown implements ApplicationComponent {

	@Override
	public void initComponent() {
		AgentManager.getAgents().forEach(PresenceAgent::init);
        AgentManager.getAgents().forEach((agent) -> agent.enable(Presence.getInstance()));
	}

	@Override
	public void disposeComponent() {
        AgentManager.getAgents().forEach(PresenceAgent::stop);
	}

}
