package com.tsunderebug.discordintellij.Slack;

import com.google.api.client.auth.oauth2.Credential;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.tsunderebug.discordintellij.Presence;
import com.tsunderebug.discordintellij.PresenceAgent;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SlackAgent extends PresenceAgent {
    private final static Logger LOG = Logger.getInstance(SlackAgent.class);
    private static final String SLACK_TEAM_MEMBERS = "presence.slack.team.members";
    private static Map<SlackTeamMember, Credential> teamMembers = new HashMap<>();

    static {
        PresenceAgent.addAgent(SlackAgent.class);
    }

    private final SlackOAuth oauth;

    @SuppressWarnings("WeakerAccess")
    public SlackAgent() {
        SlackOAuth tempOauth = null;
        try {
            tempOauth = new SlackOAuth();
        } catch (IOException e) {
            LOG.error("Unable to create Slack oauth agent.", e);
        } finally {
            oauth = tempOauth;
        }
    }

    @SuppressWarnings("unused")
    public static boolean isEnabled() {
        if (teamMembers.isEmpty()) {
            String[] members = PropertiesComponent.getInstance().getValues(SLACK_TEAM_MEMBERS);
            if (members != null && members.length > 0) {
                for (String member : members) {
                    if (member.isEmpty()) continue;
                    String[] fields = member.split(":");
                    SlackTeamMember slackTeamMember = new SlackTeamMember(fields[0], fields[1], fields[2]);
                    teamMembers.put(slackTeamMember, null);
                }
            }

        }
        return (teamMembers != null && !teamMembers.isEmpty());
    }

    public static void setEnabled(boolean enabled) {
        setEnabled(enabled, SlackAgent.class);
    }

    public static Set<SlackTeamMember> getTeams() {
        return teamMembers.keySet();
    }

    private static String buildSlackStatus(String statusText, String statusEmoji) throws UnsupportedEncodingException {
        Gson gson = new GsonBuilder().create();
        return URLEncoder.encode(gson.toJson(new SlackStatus(statusText, statusEmoji)), "UTF-8");
    }

    @Override
    public void initializeAgent() {
        if (teamMembers.isEmpty()) return;
        try {
            for (SlackTeamMember member : teamMembers.keySet()) {
                Credential credential = oauth.authorize(member.toString());
                teamMembers.put(member, credential);
            }
        } catch (SlackCredentialsNotLoadedException e) {
            LOG.warn(e);
        }

    }

    @Override
    public void showPresence(Presence presence) {
        try {
            String statusText = String.format("%s %s %s",
                    presence.getApplication(),
                    presence.getState(),
                    presence.getDetails());
            String profile = buildSlackStatus(statusText, ":intellij_idea:");
            postAllStatus(profile);
        } catch (IOException e) {
            LOG.error("Unable to encode slack presence.", e);
        }
    }

    @Override
    public void hidePresence() {
        String profile;
        try {
            profile = buildSlackStatus("", "");
            postAllStatus(profile);
        } catch (IOException e) {
            LOG.error("Unable to clear slack presence.", e);
        }
    }

    @Override
    public String getName() {
        return "Slack";
    }

    @Override
    public void stopAgent() {
        hidePresence();
    }

    public Optional<SlackTeamMember> addTeam() {
        SlackTeamMember teamMember = null;

        try {
            teamMember = oauth.addSlackTeam();
            Credential credential = oauth.authorize(teamMember.toString());
            teamMembers.put(teamMember, credential);
            persistTeamMembers();
        } catch (Exception | SlackCredentialsNotLoadedException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(teamMember);
    }

    public void removeTeam(SlackTeamMember team) {
        teamMembers.remove(team);
        oauth.removeCredential(team);
        persistTeamMembers();

    }

    private void persistTeamMembers() {
        PropertiesComponent.getInstance().setValues(SLACK_TEAM_MEMBERS,
                teamMembers.keySet().stream().map(SlackTeamMember::toShortString).toArray(String[]::new));
    }

    private void postAllStatus(String profile) throws IOException {
        IOException storedException = null;

        for (SlackTeamMember member : teamMembers.keySet()) {
            Credential credential = teamMembers.get(member);
            if (credential != null) {
                try {
                    postStatus(profile, credential);
                } catch (IOException e) {
                    storedException = e;
                }
            }
        }
        if (storedException != null) {
            throw storedException;
        }
    }

    private void postStatus(String profile, Credential credential) throws IOException {
        String url = "https://slack.com/api/users.profile.set";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty(
                "User-Agent",
                "JetBrains Rich Presence Plugin / " + SlackAgent.class.getPackage().getImplementationVersion()
        );
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "token=" + credential.getAccessToken() + "&profile=" + profile;

        // Send post request
        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(urlParameters);
        out.flush();
        out.close();

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        if (responseCode != HttpURLConnection.HTTP_OK) {
            LOG.error("Status " + responseCode + " posting slack presence.", response.toString());
        }
    }

    private static class SlackStatus {
        private String status_text;
        private String status_emoji;

        SlackStatus(String status_text, String statis_emoji) {
            this.status_text = status_text;
            this.status_emoji = statis_emoji;
        }

        @Override
        public String toString() {
            return status_text + " - " + status_emoji;
        }
    }
}
