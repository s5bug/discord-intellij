package com.tsunderebug.discordintellij;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import java.io.InputStreamReader;
import java.util.Collections;

public class SlackOAuth {
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".presence/slack-client");
    private static final String SCOPE = "users.profile:write";
    private static FileDataStoreFactory DATA_STORE_FACTORY;
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static String userId = System.getProperty("user","unknown");

    static {
        try {
            HTTP_TRANSPORT = new NetHttpTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    public static Credential authorize() throws Exception {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(SlackOAuth.class.getResourceAsStream("/slack_client_secrets.json")));
        clientSecrets.getDetails().setClientId("@slack_client_id@");
        clientSecrets.getDetails().setClientSecret("@slack_client_secret@");

        // set up authorization code flow
        AuthorizationCodeFlow flow = new AuthorizationCodeFlow.Builder(BearerToken
                .authorizationHeaderAccessMethod(),
                HTTP_TRANSPORT,
                JSON_FACTORY,
                new GenericUrl(clientSecrets.getDetails().getTokenUri()),
                new ClientParametersAuthentication(
                        clientSecrets.getDetails().getClientId(), clientSecrets.getDetails().getClientSecret()),
                clientSecrets.getDetails().getClientId(),
                clientSecrets.getDetails().getAuthUri()).setScopes(Collections.singleton(SCOPE))
                .setDataStoreFactory(DATA_STORE_FACTORY).build();


        Credential credential = flow.loadCredential(userId);
        if (credential != null) {
            return credential;
        } else {
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(50006).build();
            return new AuthorizationCodeInstalledApp(flow, receiver).authorize(userId);
        }
    }
}