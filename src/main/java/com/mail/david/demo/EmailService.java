package com.mail.david.demo;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface EmailService {
    public String sendEmail(String recipientAddress, String subject, String body, String recipientAddressBCC, List<String> filesURLs) throws GeneralSecurityException, IOException;
}
