package com.tsunderebug.discordintellij;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.project.Project;

import java.util.HashMap;
import java.util.Map;

public class DiscordIntelliJ {

	public static Map<Project, Long> openTimes = new HashMap<>();

	public static void enableRPC() {
		DiscordRPC.INSTANCE.Discord_Initialize("384215522050572288", new DiscordEventHandlers(), true, "");
		DiscordRichPresence drp = new DiscordRichPresence();
		String code = ApplicationInfo.getInstance().getBuild().asString().substring(0, 2).toLowerCase();
		drp.state = String.format("In %s %s", ApplicationInfo.getInstance().getVersionName(), ApplicationInfo.getInstance().getFullVersion());
		drp.details = ApplicationInfo.getInstance().getApiVersion();
		drp.largeImageKey = code;
		drp.largeImageText = ApplicationInfo.getInstance().getVersionName();
		drp.smallImageKey = "tsun";
		drp.smallImageText = "TsundereBug's plugin: https://goo.gl/81tZHT";
		DiscordRPC.INSTANCE.Discord_UpdatePresence(drp);
	}

	public static void enableRPC(Project p) {
		enableRPC();
		new ProjectChange().runActivity(p);
	}

	public static void hideRPC() {
		DiscordRPC.INSTANCE.Discord_ClearPresence();
	}

	public static void stopRPC() {
		DiscordRPC.INSTANCE.Discord_Shutdown();
	}

}
