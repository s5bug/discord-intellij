package com.tsunderebug.discordintellij.slack;

public class SlackCredentialNotFoundException extends Throwable {
    public SlackCredentialNotFoundException(String slackMember) {
        super(String.format("Credential not found for team '%s'.", slackMember));
    }
}
