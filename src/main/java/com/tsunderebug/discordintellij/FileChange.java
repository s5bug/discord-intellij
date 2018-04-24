package com.tsunderebug.discordintellij;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public class FileChange implements FileEditorManagerListener {

	@Override
	public void selectionChanged(FileEditorManagerEvent e) {
		Project project = e.getManager().getProject();
        Presence presence = Presence.getInstance();

		PresenceEnabled presenceEnabled = project.getComponent(PresenceEnabled.class, new PresenceEnabled());
        if(e.getNewFile() != null) {
            VirtualFile file = e.getNewFile();
            String name = file.getFileType().getDescription().split("\\s|\\.|/")[0];
            String lowercaseName = name.toLowerCase().replaceAll("#", "sharp").replaceAll("\\+", "p");
            String ide = ApplicationInfo.getInstance().getBuild().asString().substring(0, 2).toLowerCase();

            presence.setState(String.format("Working on %s", e.getManager().getProject().getName()));
            presence.setLargeImageKey(lowercaseName);
            presence.setLargeImageText(String.format("Editing a %s file", name));
            presence.setSmallImageKey(ide);
            presence.setSmallImageText(String.format("Using %s", ApplicationInfo.getInstance().getVersionName()));
            presence.setDetails(String.format("Editing [%s] %s", name, file.getName()));
            presence.setStartTimeStamp(project);
        } else {
            new ProjectChange().runActivity(project);
        }

		if(presenceEnabled.isEnabled()) {
            AgentManager.getAgents().forEach((agent) -> agent.enable(Presence.getInstance()));
		}
	}

}
