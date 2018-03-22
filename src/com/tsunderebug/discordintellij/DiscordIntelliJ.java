package com.tsunderebug.discordintellij;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.project.Project;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

import java.util.HashMap;
import java.util.Map;

public class DiscordIntelliJ {

	public static Map<Project, Long> openTimes = new HashMap<>();

	public static void enableRPC() {
		DiscordRPC.discordInitialize("384215522050572288", new DiscordEventHandlers(), true);
		DiscordRichPresence drp = new DiscordRichPresence();
		String code = ApplicationInfo.getInstance().getBuild().asString().substring(0, 2).toLowerCase();
		drp.state = String.format("In %s %s", ApplicationInfo.getInstance().getVersionName(), ApplicationInfo.getInstance().getFullVersion());
		drp.details = ApplicationInfo.getInstance().getApiVersion();
		drp.largeImageKey = code;
		drp.largeImageText = ApplicationInfo.getInstance().getVersionName();
		drp.smallImageKey = "tsun";
		drp.smallImageText = "TsundereBug's plugin: https://goo.gl/81tZHT";
		DiscordRPC.discordUpdatePresence(drp);
	}

	public static void enableRPC(Project p) {
		enableRPC();
		new ProjectChange().runActivity(p);
	}

	public static void hideRPC() {
		DiscordRPC.discordUpdatePresence(new DiscordRichPresence());
	}

	public static void stopRPC() {
		DiscordRPC.discordShutdown();
	}

}
