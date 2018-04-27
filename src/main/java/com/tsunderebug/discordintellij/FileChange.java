package com.tsunderebug.discordintellij;

import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.vfs.VirtualFile;

public class FileChange implements FileEditorManagerListener {

	@Override
	public void selectionChanged(FileEditorManagerEvent e) {
		PresenceEnabled pe = e.getManager().getProject().getComponent(PresenceEnabled.class, new PresenceEnabled());
		if(pe.isEnabled()) {
			if(e.getNewFile() != null) {
				VirtualFile file = e.getNewFile();
				String name = file.getFileType().getDescription().split("\\s|\\.|/")[0];
				String lowercaseName = name.toLowerCase().replaceAll("#", "sharp").replaceAll("\\+", "p");
				String ide = ApplicationInfo.getInstance().getBuild().asString().substring(0, 2).toLowerCase();
				DiscordRichPresence drp = new DiscordRichPresence();
				drp.state = String.format("Working on %s", e.getManager().getProject().getName());
				drp.largeImageKey = lowercaseName;
				drp.largeImageText = String.format("Editing a %s file", name);
				drp.smallImageKey = ide;
				drp.smallImageText = String.format("Using %s", ApplicationInfo.getInstance().getVersionName());
				drp.details = String.format("Editing [%s] %s", name, file.getName());
				drp.startTimestamp = DiscordIntelliJ.openTimes.get(e.getManager().getProject()) / 1000;
                DiscordRPC.INSTANCE.Discord_UpdatePresence(drp);
			} else {
				new ProjectChange().runActivity(e.getManager().getProject());
			}
		}
	}

}
