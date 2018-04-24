package com.tsunderebug.discordintellij;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import org.jetbrains.annotations.NotNull;

public class ProjectChange implements StartupActivity {

	@Override
	public void runActivity(@NotNull Project project) {
		PresenceEnabled pe = project.getComponent(PresenceEnabled.class, new PresenceEnabled());
		if(pe.isEnabled()) {
			DiscordIntelliJ.openTimes.put(project, System.currentTimeMillis());
			String code = ApplicationInfo.getInstance().getBuild().asString().substring(0, 2).toLowerCase();
			FileChange hook = new FileChange();
			project.getMessageBus().connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, hook);
			DiscordRichPresence drp = new DiscordRichPresence();
			drp.state = String.format("Opened %s", project.getName());
			drp.details = String.format("%s", ApplicationInfo.getInstance().getApiVersion());
			drp.largeImageKey = code;
			drp.largeImageText = ApplicationInfo.getInstance().getVersionName();
			drp.smallImageKey = "tsun";
			drp.smallImageText = "TsundereBug's plugin: https://goo.gl/81tZHT";
			drp.startTimestamp = DiscordIntelliJ.openTimes.get(project) / 1000;
			DiscordRPC.discordUpdatePresence(drp);
		}
	}

}
