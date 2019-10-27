package com.mail.david.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.security.GeneralSecurityException;





@SpringBootApplication
public class GmailApplication {
    public static void main(String... args) throws IOException, GeneralSecurityException {
        SpringApplication.run(GmailApplication.class, args);

    }



    }



