package com.base.java.project;

import logic.Calculate;
import model.DataToCalculate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = "/")

public class MainController {


    @GetMapping("/pow3")
    ResponseEntity pow3(@RequestParam int number, int pow)
    {
        ResponseEntity httpResponse = new ResponseEntity(Math.pow(number,pow),HttpStatus.OK);
        System.out.println("print aaa");
        System.out.println(Thread.currentThread().getName());
        return httpResponse;
    }


    @PostMapping(value = "/calc")
    public ResponseEntity calculatePow(@RequestBody DataToCalculate number) {
        Calculate c= new Calculate();
        System.out.println(Thread.currentThread().getName());
        Integer result = c.calculatePow(number.getX());
        ResponseEntity httpResponse = new ResponseEntity(result,HttpStatus.OK);
        return httpResponse;
    }




}
