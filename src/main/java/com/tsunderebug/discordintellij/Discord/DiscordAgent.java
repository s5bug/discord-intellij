package com.tsunderebug.discordintellij.Discord;


import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.intellij.ide.util.PropertiesComponent;
import com.tsunderebug.discordintellij.Presence;
import com.tsunderebug.discordintellij.PresenceAgent;

public class DiscordAgent extends PresenceAgent {
    public static final String DISCORD_PRESENCE_ENABLED = "presence.discord.enabled";

    static {
        PresenceAgent.addAgent(DiscordAgent.class);
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
        DiscordRPC.INSTANCE.Discord_Initialize("384215522050572288", new DiscordEventHandlers(), true, "");
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
        drp.state = presence.getState();
        drp.details = presence.getDetails();
        drp.largeImageKey = presence.getLargeImageKey();
        drp.largeImageText = presence.getLargeImageText();
        drp.smallImageKey = presence.getSmallImageKey();
        drp.smallImageText = presence.getSmallImageText();
        drp.startTimestamp = presence.getStartTimeStamp();
        return drp;
    }
}
