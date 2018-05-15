package com.tsunderebug.discordintellij.Slack;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;

import java.io.IOException;

class SlackAuthorizationApp extends AuthorizationCodeInstalledApp {
    /**
     * @param flow     authorization code flow
     * @param receiver verification code receiver
     */
    public SlackAuthorizationApp(AuthorizationCodeFlow flow, VerificationCodeReceiver receiver) {
        super(flow, receiver);
    }

    @Override
    protected void onAuthorization(AuthorizationCodeRequestUrl authorizationUrl) throws IOException {
        System.err.println(getFlow().getClientId());
        super.onAuthorization(authorizationUrl);
        System.err.println(getFlow().getClientId());
    }
}
