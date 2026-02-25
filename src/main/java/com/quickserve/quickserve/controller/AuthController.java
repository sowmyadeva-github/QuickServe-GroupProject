package com.quickserve.quickserve.controller;

import com.quickserve.quickserve.entity.User;
import com.quickserve.quickserve.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return service.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        User loggedUser = service.login(user.getEmail(), user.getPassword());

        if(loggedUser == null){
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        return ResponseEntity.ok(loggedUser);
    }
}