package com.mail.david.demo;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.mail.david.demo.google.GmailCredentials;
import com.mail.david.demo.google.GmailService;
import com.mail.david.demo.google.GmailServiceImpl;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class EmailServiceImpl implements EmailService {
    private static final String clientId = "385161064720-th3r07fpo2kn32npos7v3skirpaq414r.apps.googleusercontent.com";
    private static final String clientSecret = "RKpuNO80P1UjfQF1G08eiJLY";
    private static final String APPLICATION_NAME = "Gmail API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_PATH = ".";


    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static List<String> SCOPES = new ArrayList();

    static {
        SCOPES.add(GmailScopes.GMAIL_LABELS);
        SCOPES.add(GmailScopes.GMAIL_SEND);
    }

    private static final String CREDENTIALS_FILE_PATH = "/credentials-gmail.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GmailApplication.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    @Override
    public String sendEmail(String recipientAddress, String subject, String body, String recipientAddressBCC,List<String> filesURLs) throws GeneralSecurityException, IOException {
        // Build a new authorized API client service.
        String response="OK";
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        // Print the labels in the user's account.
        String user = "do-not-reply@kvisi.com";
        ListLabelsResponse listResponse = service.users().labels().list(user).execute();
        List<Label> labels = listResponse.getLabels();
        if (labels.isEmpty()) {
            System.out.println("No labels found.");
        } else {
            System.out.println("Labels:");
            for (Label label : labels) {
                System.out.printf("- %s\n", label.getName());
            }
        }
        try {
            com.google.api.client.auth.oauth2.Credential credentials = getCredentials(HTTP_TRANSPORT);
            GmailService gmailService = new GmailServiceImpl(GoogleNetHttpTransport.newTrustedTransport());
            gmailService.setGmailCredentials(GmailCredentials.builder()
                    .userEmail(user)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .accessToken(credentials.getAccessToken())
                    .refreshToken(credentials.getRefreshToken())
                    .build());

            gmailService.sendMessage(recipientAddress, subject, body,
                    recipientAddressBCC,filesURLs);
        } catch (GeneralSecurityException | IOException | MessagingException e) {
            response=e.getMessage();
            e.printStackTrace();
        }
        return response;
    }

}
