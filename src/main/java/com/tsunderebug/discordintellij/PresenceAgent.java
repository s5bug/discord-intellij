package com.tsunderebug.discordintellij;

import com.intellij.openapi.project.Project;

public abstract class PresenceAgent {
    public abstract void init();
    public abstract void enable(Presence presence);
    public abstract void hide();
    public abstract void stop();
}
