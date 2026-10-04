package com.financetracker.controller;

import com.financetracker.model.User;
import com.financetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user) {
        try {
            User savedUser = userService.registerUser(user);
            // Hide password in response for safety
            savedUser.setPassword(null);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: Username may already exist.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody User user) {
        Optional<User> foundUser = userService.loginUser(user.getUsername(), user.getPassword());
        if (foundUser.isPresent()) {
            User loggedInUser = foundUser.get();
            loggedInUser.setPassword(null); // Hide password in response
            return ResponseEntity.ok(loggedInUser);
        }
        return ResponseEntity.status(401).body("Invalid username or password");
    }
}