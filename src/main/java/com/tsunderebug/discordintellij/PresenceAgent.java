package com.tsunderebug.discordintellij;

public abstract class PresenceAgent {
    private boolean enabled = false;
    private boolean active = true;

    public abstract void init();
    public abstract void enable(Presence presence);
    public abstract void hide();
    public abstract void stop();

    public abstract String getName();


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void togglePresence() {
        active = !active;
    }

    public void update(Presence presence) {
        if (active) {
            enable(presence);
        } else {
            hide();
        }
    }
}
