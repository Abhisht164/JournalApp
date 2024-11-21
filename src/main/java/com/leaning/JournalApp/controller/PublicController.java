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

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        // Check if a user with the same username already exists
        User existingUser = userService.findByUserName(user.getUserName());
        if (existingUser != null) {
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST); // Return 400 Bad Request if user exists
        }

        // Save the new user
        User newUser = userService.saveUser(user);

        // Return a success message with the newly created use
        return new ResponseEntity<>(newUser, HttpStatus.CREATED); // Return 201 Created with the new user data
    }
}
