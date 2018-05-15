package com.tsunderebug.discordintellij.Slack;

public class SlackCredentialsNotLoadedException extends Throwable {
    public SlackCredentialsNotLoadedException(Throwable e) {
        super("Error loading credentials from password database.", e);
    }
}
