package com.mail.david.demo.google;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface GmailService {
    void setGmailCredentials(GmailCredentials gmailCredentials);
    boolean sendMessage(String recipientAddress, String subject, String body, String recipientAddressBCC, List<String> fileURLs) throws MessagingException, IOException;
}
