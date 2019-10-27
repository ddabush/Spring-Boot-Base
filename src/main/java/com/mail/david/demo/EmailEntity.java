package com.mail.david.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class EmailEntity {
    private String  recipientAddress;
    private String  subject;
    private String  body;
    private String recipientAddressBCC;
    private String password;
    private List<String> fileURLs;

}
