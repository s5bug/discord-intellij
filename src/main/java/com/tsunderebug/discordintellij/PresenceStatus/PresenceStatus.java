package com.tsunderebug.discordintellij.PresenceStatus;

import com.intellij.notification.Notification;
import com.intellij.openapi.progress.util.BackgroundTaskUtil;
import com.intellij.util.messages.Topic;

import static com.tsunderebug.discordintellij.PresenceStatus.PresenceStatus.PRESENCE_STATUS_CHANGE;

public class PresenceStatus {
    public static final Topic<PresenceStatusChangeListener> PRESENCE_STATUS_CHANGE = Topic.create("Presence Status change", PresenceStatusChangeListener.class);;
    private static final String DEFAULT_PRESENCE_STATUS = "Available";

    private String status;

    public PresenceStatus() {
        this(DEFAULT_PRESENCE_STATUS);
    }

    public PresenceStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (!this.status.equals(status)) {
            this.status = status;
            BackgroundTaskUtil.syncPublisher(PRESENCE_STATUS_CHANGE).statusChanged(status);
        }
    }
}
