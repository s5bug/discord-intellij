package com.tsunderebug.discordintellij;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;

public class StartupShutdown implements ApplicationComponent {

	@Override
	public void initComponent() {
        AgentManager.initializeAgents();
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                AgentManager.getAgents().forEach(PresenceAgent::init);
                AgentManager.getAgents().forEach((agent) -> agent.show(Presence.getInstance()));
            }
        });
	}

	@Override
	public void disposeComponent() {
        AgentManager.getAgents().forEach(PresenceAgent::stop);
	}

}
