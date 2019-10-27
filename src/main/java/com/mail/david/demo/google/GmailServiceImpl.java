package com.mail.david.demo.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.google.api.services.gmail.model.SendAs;
import org.apache.commons.io.FileUtils;
import org.springframework.util.CollectionUtils;

public final class GmailServiceImpl implements GmailService {

    private static final String APPLICATION_NAME = "YOUR_APP_NAME";

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private final HttpTransport httpTransport;
    private GmailCredentials gmailCredentials;

    public GmailServiceImpl(HttpTransport httpTransport) {
        this.httpTransport = httpTransport;
    }

    @Override
    public void setGmailCredentials(GmailCredentials gmailCredentials) {
        this.gmailCredentials = gmailCredentials;
    }

    @Override
    public boolean sendMessage(String recipientAddress, String subject, String body, String recipientAddressBCC, List<String> fileURLs) throws MessagingException,
            IOException {
        Message message = createMessageWithEmail(
                createEmail(recipientAddress, gmailCredentials.getUserEmail(), subject, body, recipientAddressBCC, fileURLs));

        return createGmail().users()
                .messages()
                .send(gmailCredentials.getUserEmail(), message)
                .execute()
                .getLabelIds().contains("SENT");

    }

    private Gmail createGmail() {
        Credential credential = authorize();
        return new Gmail.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private MimeMessage createEmail(String to, String from, String subject, String bodyText, String toBCC, List<String> fileURLs) throws MessagingException {
        MimeMessage email = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
        Multipart multiPart = new MimeMultipart("mixed");
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(bodyText, "text/html; charset=utf-8");
        multiPart.addBodyPart(htmlPart);
        email.setContent(multiPart);
        email.setFrom(new InternetAddress("kVisi Team <do-not-reply@kvisi.com>"));
        email.addRecipients(javax.mail.Message.RecipientType.TO, to);
        email.setSubject(subject);
        email.addRecipients(javax.mail.Message.RecipientType.BCC, toBCC);
        if (!CollectionUtils.isEmpty(fileURLs)) {
            AddAttachments(fileURLs, email, multiPart);
        }

        return email;
    }

    private List<File> AddAttachments(List<String> fileURLs, MimeMessage email, Multipart multiPart) throws MessagingException {
        List<File> files = getFileFromUrls(fileURLs);
        for (File file : files) {
            MimeBodyPart mimeBodyPartAttachment = new MimeBodyPart();
            DataSource source = new FileDataSource(file);
            mimeBodyPartAttachment.setDataHandler(new DataHandler(source));
            mimeBodyPartAttachment.setFileName(file.getName());
            multiPart.addBodyPart(mimeBodyPartAttachment);
        }
        email.setContent(multiPart);
        return files;


    }

    private List<File> getFileFromUrls(List<String> urls) {
        List<File> files = new ArrayList<File>();
        for (String url : urls) {
            try {
                String toFile = getFileName(url);
                File file = new File(toFile);
                FileUtils.copyURLToFile(new URL(url), file, 10000, 10000);
                files.add(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return files;
    }

    private String getFileName(String url) {
        String[] urlParts = url.split("/");
        String fileName = "file1";
        if (urlParts.length > 0) {
            fileName = urlParts[urlParts.length - 1];
        }
        return fileName;

    }

    private Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);

        return new Message()
                .setRaw(Base64.encodeBase64URLSafeString(buffer.toByteArray()));
    }

    private Credential authorize() {
        return new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(gmailCredentials.getClientId(), gmailCredentials.getClientSecret())
                .build()
                .setAccessToken(gmailCredentials.getAccessToken())
                .setRefreshToken(gmailCredentials.getRefreshToken());
    }
}
