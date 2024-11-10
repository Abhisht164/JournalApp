package com.leaning.JournalApp.controller;

import com.leaning.JournalApp.entity.User;
import com.leaning.JournalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    UserService userService;


    @GetMapping("/health-check")
    public String healthCheck(){
        return "OK";
    }

}
