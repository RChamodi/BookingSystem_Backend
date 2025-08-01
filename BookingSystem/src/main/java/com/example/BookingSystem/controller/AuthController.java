package com.example.BookingSystem.controller;

import com.example.BookingSystem.entity.User;
import com.example.BookingSystem.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;



    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @GetMapping("/me")
    public User getProfile(Principal principal) {
        return userService.findByEmail(principal.getName());
    }

    @PutMapping("/me")
    public void updateProfile(Principal principal, @RequestBody User updates) {
        User user = userService.findByEmail(principal.getName());
        userService.updateProfile(user.getId(), updates);
    }

    @PutMapping("/me/password")
    public void updatePassword(Principal principal, @RequestBody String newPassword) {
        User user = userService.findByEmail(principal.getName());
        userService.updatePassword(user.getId(), newPassword);
    }

}
