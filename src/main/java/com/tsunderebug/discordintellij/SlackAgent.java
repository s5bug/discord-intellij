package com.tsunderebug.discordintellij;

import com.google.api.client.auth.oauth2.Credential;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.diagnostic.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;

public class SlackAgent extends PresenceAgent {
    private final static Logger LOG = Logger.getInstance(SlackAgent.class);
    private final static Credential credential;

    static {
        Credential tempCredential = null;
        try {
            tempCredential = SlackOAuth.authorize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        credential = tempCredential;
    }

    @Override
    public void init() {
    }

    @Override
    public void enable(Presence presence) {
        try {
            String statusText = String.format("%s %s %s",
                    presence.getApplication(),
                    presence.getState(),
                    presence.getDetails());
            String profile = buildSlackStatus(statusText,":intellij_idea:");
            postStatus(profile);
        } catch (IOException e) {
            LOG.error("Unable to encode slack presence.", e);
        }
    }

    @Override
    public void hide() {
        String profile;
        try {
            profile = buildSlackStatus("", "");
            postStatus(profile);
        } catch (IOException e) {
            LOG.error("Unable to clear slack presence.", e);
        }
    }

    @Override
    public void stop() {
        hide();
    }

    private String buildSlackStatus(String statusText, String statusEmoji) throws UnsupportedEncodingException {
        Gson gson = new GsonBuilder().create();
        return URLEncoder.encode(gson.toJson(new SlackStatus(statusText, statusEmoji)), "UTF-8");
    }

    private void postStatus(String profile) throws IOException {
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

        String urlParameters = "token="+credential.getAccessToken()+"&profile="+profile;

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

    private class SlackStatus {
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
