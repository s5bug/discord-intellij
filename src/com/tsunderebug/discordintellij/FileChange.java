package com.tsunderebug.discordintellij;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.vfs.VirtualFile;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

public class FileChange implements FileEditorManagerListener {

	@Override
	public void selectionChanged(FileEditorManagerEvent e) {
		PresenceEnabled pe = e.getManager().getProject().getComponent(PresenceEnabled.class, new PresenceEnabled());
		if(pe.isEnabled()) {
			if(e.getNewFile() != null) {
				VirtualFile file = e.getNewFile();
				String name = file.getFileType().getDescription();
				String lowercaseName = name.split("\\s")[0].toLowerCase().replaceAll("#", "sharp");
				String ide = ApplicationInfo.getInstance().getBuild().asString().substring(0, 2).toLowerCase();
				DiscordRichPresence drp = new DiscordRichPresence();
				drp.state = String.format("Working on %s", e.getManager().getProject().getName());
				drp.largeImageKey = lowercaseName;
				drp.largeImageText = String.format("Editing a %s file", name);
				drp.smallImageKey = ide;
				drp.smallImageText = String.format("Using %s", ApplicationInfo.getInstance().getVersionName());
				drp.details = String.format("Editing [%s] %s", name, file.getName());
				drp.startTimestamp = DiscordIntelliJ.openTimes.get(e.getManager().getProject()) / 1000;
				DiscordRPC.discordUpdatePresence(drp);
			} else {
				new ProjectChange().runActivity(e.getManager().getProject());
			}
		}
	}

}
