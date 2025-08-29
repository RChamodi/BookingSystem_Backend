package com.example.BookingSystem.controller;

import com.example.BookingSystem.entity.User;
import com.example.BookingSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @PutMapping("/users/{id}/block")
    public void blockUser(@PathVariable Long id) {
        userService.setBlocked(id, true);
    }

    @PutMapping("/users/{id}/unblock")
    public void unblockUser(@PathVariable Long id) {
        userService.setBlocked(id, false);
    }
}
