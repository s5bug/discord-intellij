package com.tsunderebug.discordintellij;


import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class DiscordAgent extends PresenceAgent {

	@Override
    public void init() {
        DiscordRPC.INSTANCE.Discord_Initialize("384215522050572288", new DiscordEventHandlers(), true, "");
    }

    @Override
    public void enable(Presence presence) {
        DiscordRPC.INSTANCE.Discord_UpdatePresence(getPresence(presence));
    }

    @Override
    public void hide() {
        DiscordRPC.INSTANCE.Discord_ClearPresence();
    }

    @Override
    public void stop() {
        DiscordRPC.INSTANCE.Discord_Shutdown();
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
