package com.kirilldergunov.server.controller;

import com.kirilldergunov.server.dao.UserRepository;
import com.kirilldergunov.server.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/change-password")
    public void changePassword(@RequestBody String newPassword,
                               Authentication auth) {

        User user = userRepository
                .findByUsername(auth.getName())
                .orElseThrow();

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordChanged(true);

        userRepository.save(user);
    }
}