package com.mail.david.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Controller
@RequestMapping(value = "/mail")

public class MailController {
    @Autowired
    private EmailService emailService=null;
    private final String password="fjdsbjfbdskfndslf3243kkkabnatan111ydkd7";

    @GetMapping("/health")
    ResponseEntity health()
    {
        ResponseEntity httpResponse = new ResponseEntity(HttpStatus.OK);
        return httpResponse;
    }


    @PostMapping("/send")
    ResponseEntity sendEmail(@RequestBody EmailEntity emailEntity) throws GeneralSecurityException, IOException {
        ResponseEntity httpResponse = new ResponseEntity(HttpStatus.OK);
        String requestPassword = emailEntity.getPassword();
        if(!password.equals(requestPassword)){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        String body=emailEntity.getBody();
        String subject=emailEntity.getSubject();
        String recipientAddress=emailEntity.getRecipientAddress();
        String recipientAddressBCC=emailEntity.getRecipientAddressBCC();
        List<String> fileUrls=emailEntity.getFileURLs();
        String response=emailService.sendEmail(recipientAddress,subject,body,recipientAddressBCC,fileUrls);

        if(!"OK".equals(response)){
            httpResponse = new ResponseEntity(response,HttpStatus.NOT_ACCEPTABLE);
        }
        return httpResponse;
    }

    @Bean
    EmailService initDatabase() {
        EmailServiceImpl emailService = new EmailServiceImpl();
        return emailService;
    }


}
