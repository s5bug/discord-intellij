package com.tsunderebug.discordintellij;

import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

public class ProjectChange implements StartupActivity {

	@Override
	public void runActivity(@NotNull Project project) {
	    Presence presence = Presence.getInstance();
		presence.setProjectOpenTime(project, System.currentTimeMillis());
        FileChange hook = new FileChange();
        project.getMessageBus().connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, hook);

        AgentManager.getAgents().forEach(agent -> {
            agent.setCurrentProject(project);
            agent.show(Presence.getInstance());
        });
	}

}
