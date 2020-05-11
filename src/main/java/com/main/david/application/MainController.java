package com.main.david.application;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/main")

public class MainController {


    @GetMapping("/health")
    ResponseEntity health()
    {
        ResponseEntity httpResponse = new ResponseEntity("bla bla",HttpStatus.OK);
        return httpResponse;
    }







}
