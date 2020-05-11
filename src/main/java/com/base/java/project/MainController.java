package com.base.java.project;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Controller
@RequestMapping(value = "/main")

public class MainController {


    @GetMapping("/health")
    ResponseEntity health()
    {
        ResponseEntity httpResponse = new ResponseEntity("bla bla",HttpStatus.OK);
        return httpResponse;
    }


    @PostMapping("/send")
    ResponseEntity send() throws GeneralSecurityException, IOException {
        ResponseEntity httpResponse = new ResponseEntity(HttpStatus.OK);

        return httpResponse;
    }




}
