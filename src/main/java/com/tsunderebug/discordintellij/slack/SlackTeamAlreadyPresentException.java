package com.tsunderebug.discordintellij.slack;

public class SlackTeamAlreadyPresentException extends IllegalArgumentException {
    public SlackTeamAlreadyPresentException(String teamName) {
        super(String.format("Slack team '%s' has already been configured.", teamName));
    }
}
