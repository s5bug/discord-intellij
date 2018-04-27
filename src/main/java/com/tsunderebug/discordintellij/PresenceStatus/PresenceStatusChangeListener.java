package com.tsunderebug.discordintellij.PresenceStatus;

import org.jetbrains.annotations.NotNull;

public interface PresenceStatusChangeListener {
    void statusChanged(@NotNull String status);
}
