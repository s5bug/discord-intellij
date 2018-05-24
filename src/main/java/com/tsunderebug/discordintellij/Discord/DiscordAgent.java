package com.tsunderebug.discordintellij.Discord;


import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.intellij.ide.util.PropertiesComponent;
import com.tsunderebug.discordintellij.Presence;
import com.tsunderebug.discordintellij.PresenceAgent;

import static com.tsunderebug.discordintellij.Discord.DiscordAPIKeys.DISCORD_CLIENT_ID;

public class DiscordAgent extends PresenceAgent {
    public static final String DISCORD_PRESENCE_ENABLED = "presence.discord.enabled";
    private static final String SMALL_IMAGE_KEY = "tsun";
    private static final String SMALL_IMAGE_TEXT = "TsundereBug's plugin: https://goo.gl/81tZHT";


    static {
        PresenceAgent.addAgent(DiscordAgent.class);
    }

    public DiscordAgent() {
        super(new DiscordTogglePresence());
    }

    @SuppressWarnings("unused")
    public static boolean isEnabled() {
        return PropertiesComponent.getInstance().getBoolean(DISCORD_PRESENCE_ENABLED, true);
    }

    public static void setEnabled(boolean enabled) {
        setEnabled(enabled, DiscordAgent.class);
    }

    @Override
    public void initializeAgent() {
        DiscordRPC.INSTANCE.Discord_Initialize(DISCORD_CLIENT_ID, new DiscordEventHandlers(), true, "");
    }

    @Override
    public void showPresence(Presence presence) {
        DiscordRPC.INSTANCE.Discord_UpdatePresence(getPresence(presence));
    }

    @Override
    public void hidePresence() {
        DiscordRPC.INSTANCE.Discord_ClearPresence();
    }

    @Override
    public void stopAgent() {
        DiscordRPC.INSTANCE.Discord_Shutdown();
    }

    @Override
    public String getName() {
        return "Discord";
    }

    private DiscordRichPresence getPresence(Presence presence) {
        DiscordRichPresence drp = new DiscordRichPresence();
        drp.state = String.format("In %s %s", presence.getVersionName(), presence.getFullVersion());
        drp.details = presence.getApiVersion();
        drp.largeImageKey = presence.getBuild().substring(0, 2).toLowerCase();
        drp.largeImageText = presence.getVersionName();
        drp.smallImageKey = SMALL_IMAGE_KEY;
        drp.smallImageText = SMALL_IMAGE_TEXT;
        drp.startTimestamp = presence.getStartTimeStamp();
        return drp;
    }
}
