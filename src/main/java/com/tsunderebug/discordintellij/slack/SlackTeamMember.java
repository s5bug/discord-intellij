package com.tsunderebug.discordintellij.slack;

public class SlackTeamMember {
    private final String teamId;
    private final String teamName;
    private final String userId;

    public SlackTeamMember(String teamId, String teamName, String userId) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.userId = userId;
    }

    public String getTeamName() {
        return teamName;
    }

    @Override
    public String toString() {
        return "SlackTeamMember{" +
                "teamId='" + teamId + '\'' +
                ", teamName='" + teamName + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    public String toShortString() {
        return String.join(":", teamId, teamName, userId);
    }

    public String getTeamId() {
        return teamId;
    }

    public String getUserId() {
        return userId;
    }
}
