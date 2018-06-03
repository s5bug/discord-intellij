package com.tsunderebug.discordintellij.slack;

import com.google.api.client.auth.oauth2.*;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Preconditions;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.diagnostic.Logger;
import com.otabi.PasswordSafeDataStoreFactory;

import java.awt.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Collections;

import static com.tsunderebug.discordintellij.slack.SlackAPIKeys.SLACK_OAUTH_CLIENT_ID;
import static com.tsunderebug.discordintellij.slack.SlackAPIKeys.SLACK_OAUTH_CLIENT_SECRET;

public class SlackOAuth {
    private static final Logger LOGGER = com.intellij.openapi.diagnostic.Logger
            .getInstance(SlackOAuth.class.getName());

    private static final String SCOPE = "users.profile:write";
    private static final String CLIENT_ID = SLACK_OAUTH_CLIENT_ID;
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static PasswordSafeDataStoreFactory DATA_STORE_FACTORY;
    private static HttpTransport HTTP_TRANSPORT;
    private static GoogleClientSecrets clientSecrets;

    static {
        try {
            HTTP_TRANSPORT = new NetHttpTransport();
            DATA_STORE_FACTORY = new PasswordSafeDataStoreFactory();
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                    new InputStreamReader(SlackOAuth.class.getResourceAsStream("/slack_client_secrets.json")));
            clientSecrets.getDetails().setClientId(CLIENT_ID);
            clientSecrets.getDetails().setClientSecret(SLACK_OAUTH_CLIENT_SECRET);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    private final AuthorizationCodeFlow flow;
    private final LocalServerReceiver receiver;

    SlackOAuth() throws IOException {
        flow = new AuthorizationCodeFlow.Builder(BearerToken
                .authorizationHeaderAccessMethod(),
                HTTP_TRANSPORT,
                JSON_FACTORY,
                new GenericUrl(clientSecrets.getDetails().getTokenUri()),
                new ClientParametersAuthentication(
                        clientSecrets.getDetails().getClientId(), clientSecrets.getDetails().getClientSecret()),
                clientSecrets.getDetails().getClientId(),
                clientSecrets.getDetails().getAuthUri()).setScopes(Collections.singleton(SCOPE))
                .setDataStoreFactory(DATA_STORE_FACTORY).build();

        receiver = new LocalServerReceiver.Builder().setPort(50006).build();

    }

    private static void browse(String url) {
        Preconditions.checkNotNull(url);
        // Ask user to open in their browser using copy-paste
        System.out.println("Please open the following address in your browser:");
        System.out.println("  " + url);
        // Attempt to open it in the browser
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    System.out.println("Attempting to open that address in the default browser now...");
                    desktop.browse(URI.create(url));
                }
            }
        } catch (IOException | InternalError e) {
            LOGGER.warn("Unable to open browser", e);
        }
    }

    public SlackTeamMember addSlackTeam() throws Exception {
        try {
            // open in browser
            String redirectUri = receiver.getRedirectUri();
            AuthorizationCodeRequestUrl authorizationUrl =
                    flow.newAuthorizationUrl().setRedirectUri(redirectUri);
            browse(authorizationUrl.build());
            // receive authorization code and exchange it for an access token
            String code = receiver.waitForCode();
            TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
            SlackTeamMember member = new SlackTeamMember(
                    response.get("team_id").toString(),
                    response.get("team_name").toString(),
                    response.get("user_id").toString());

            // store credential
            flow.createAndStoreCredential(response, member.toString());
            return member;
        } finally {
            receiver.stop();
        }
    }

    public Credential authorize(String slackMember) throws SlackCredentialsNotLoadedException {
        try {
            Credential credential = flow.loadCredential(slackMember);
            if (credential == null) {
                throw new SlackCredentialNotFoundException(slackMember);
            }
            return credential;
        } catch (IOException | SlackCredentialNotFoundException e) {
            Notifications.Bus.notify(
                    new Notification(
                            "slack presence",
                            "Unable to authorize team " + slackMember + ".",
                            e.getMessage(),
                            NotificationType.ERROR));
            throw new SlackCredentialsNotLoadedException(e);
        }
    }

    public void removeCredential(SlackTeamMember team) {
        try {
            flow.getCredentialDataStore().delete(team.toString());
        } catch (IOException e) {
            Notifications.Bus.notify(
                    new Notification(
                            "presence",
                            "Unable to remove credential for Slack teaam" + team.getTeamName() + ".",
                            e.toString(),
                            NotificationType.WARNING));
        }
    }
}