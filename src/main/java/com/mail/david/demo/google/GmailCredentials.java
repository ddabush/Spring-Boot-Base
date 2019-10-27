package com.mail.david.demo.google;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GmailCredentials {
    private  String userEmail;
    private  String clientId;
    private  String clientSecret;
    private  String accessToken;
    private  String refreshToken;


}