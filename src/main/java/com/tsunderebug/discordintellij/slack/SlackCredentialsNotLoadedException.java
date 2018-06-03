package com.tsunderebug.discordintellij.slack;

public class SlackCredentialsNotLoadedException extends Throwable {
    public SlackCredentialsNotLoadedException(Throwable e) {
        super("Error loading credentials from password database.", e);
    }
}
