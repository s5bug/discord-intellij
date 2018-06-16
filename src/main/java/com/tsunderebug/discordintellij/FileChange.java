package com.tsunderebug.discordintellij;

import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;

public class FileChange implements FileEditorManagerListener {

	@Override
	public void selectionChanged(FileEditorManagerEvent e) {
		Project project = e.getManager().getProject();
        Presence presence = Presence.getInstance();

        if(e.getNewFile() != null) {
            presence.setFile(e.getNewFile().getName(), e.getNewFile().getFileType().getDescription());
        } else {
            presence.clearFile();
            new ProjectChange().runActivity(project);
        }
	}
}
