package com.leaning.JournalApp.controller;


import com.leaning.JournalApp.entity.User;
import com.leaning.JournalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserEntryController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> getAll() {
        List<User> allUsers = userService.getAllUsers();

        if (allUsers.isEmpty()) {
            return new ResponseEntity<>("No users found", HttpStatus.NO_CONTENT); // 204 No Content if no users exist
        }

        return new ResponseEntity<>(allUsers, HttpStatus.OK); // 200 OK if users are found
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


    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        User userInDb = userService.findByUserName(user.getUserName());

        if (userInDb != null) {
            System.out.println("Updated user: " + userInDb.getUserName());

            userInDb.setUserName(user.getUserName());
            userInDb.setPassword(user.getPassword());
            userInDb.setEmail(user.getEmail());

            User savedUser = userService.saveUser(userInDb);
            return new ResponseEntity<>(savedUser, HttpStatus.OK); // Return 200 OK with updated user data
        }

        System.out.println("User not found: " + user.getUserName());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 if user not found
    }


    @DeleteMapping("username/{userName}")
    public ResponseEntity<?> deleteUserByName(@PathVariable String userName) {
        User userInDb = userService.findByUserName(userName);

        if (userInDb != null) {
            userService.deleteByUserName(userName);
            System.out.println("User deleted successfully: " + userName);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Success, no content to return
        } else {
            System.out.println("User not found: " + userName);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // User not found
        }
    }

}
