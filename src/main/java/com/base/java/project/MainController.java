package com.base.java.project;

import logic.Calculate;
import model.DataToCalculate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Controller
@RequestMapping(value = "/")

public class MainController {


    @GetMapping("/health")
    ResponseEntity health()
    {
        ResponseEntity httpResponse = new ResponseEntity("bla bla",HttpStatus.OK);
        return httpResponse;
    }


    @PostMapping(value = "/calc")
    public ResponseEntity calculatePow(@RequestBody DataToCalculate number) {
        Calculate c= new Calculate();
        Integer result = c.calculatePow(number.getX());
        ResponseEntity httpResponse = new ResponseEntity(result,HttpStatus.OK);
        return httpResponse;
    }




}
