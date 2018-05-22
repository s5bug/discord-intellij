package com.tsunderebug.discordintellij;

public class PresenceActive {
    private boolean active;

    public PresenceActive() {
        this(true);
    }

    public PresenceActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
