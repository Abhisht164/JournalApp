package com.leaning.JournalApp.service;

import com.leaning.JournalApp.entity.User;
import com.leaning.JournalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    private static final PasswordEncoder passwordencoder=new BCryptPasswordEncoder();

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Object myId){
        return userRepository.findById(myId);
    }

    public User saveUser(User user) {
        user.setPassword(passwordencoder.encode(user.getPassword()));
        user.setRoles(List.of("user"));
        return userRepository.save(user);
    }

    public User saveNewUser(User user) {
        return userRepository.save(user);
    }

    public User findByUserName(String username){
        return userRepository.findByUserName(username);
    }

    public void deleteByUserName(String username){
        userRepository.deleteByUserName(username);
    }
}
