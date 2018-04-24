package com.tsunderebug.discordintellij;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

public class ProjectChange implements StartupActivity {

	@Override
	public void runActivity(@NotNull Project project) {
	    Presence presence = Presence.getInstance();
		presence.setProjectOpenTime(project, System.currentTimeMillis());
        String code = ApplicationInfo.getInstance().getBuild().asString().substring(0, 2).toLowerCase();
        FileChange hook = new FileChange();
        project.getMessageBus().connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, hook);
        presence.setState(String.format("Opened %s", project.getName()));
        presence.setDetails(String.format("%s", ApplicationInfo.getInstance().getApiVersion()));
        presence.setLargeImageKey(code);
        presence.setLargeImageText(ApplicationInfo.getInstance().getVersionName());

		PresenceEnabled presenceEnabled = project.getComponent(PresenceEnabled.class, new PresenceEnabled());
		if(presenceEnabled.isEnabled()) {
			AgentManager.getAgents().forEach(x -> x.enable(Presence.getInstance()));
		}
	}

}
