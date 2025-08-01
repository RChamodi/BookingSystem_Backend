package com.example.BookingSystem.service;

import com.example.BookingSystem.entity.User;
import com.example.BookingSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void updateProfile(Long id, User updates) {
        User user = userRepository.findById(id).orElseThrow();
        user.setName(updates.getName());
        user.setContactInfo(updates.getContactInfo());
        user.setPreferences(updates.getPreferences());
        userRepository.save(user);
    }

    public void updatePassword(Long id, String newPassword) {
        User user = userRepository.findById(id).orElseThrow();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void setBlocked(Long id, boolean blocked) {
        User user = userRepository.findById(id).orElseThrow();
        user.setBlocked(blocked);
        userRepository.save(user);
    }
}
