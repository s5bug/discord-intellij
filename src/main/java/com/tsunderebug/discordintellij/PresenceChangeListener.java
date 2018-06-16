package com.tsunderebug.discordintellij;

public interface PresenceChangeListener {
    void projectChanged(Presence presence);

    void fileChanged(Presence presence);
}
