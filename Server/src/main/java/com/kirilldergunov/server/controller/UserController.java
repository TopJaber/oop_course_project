package com.kirilldergunov.server.controller;

import com.kirilldergunov.server.dao.UserRepository;
import com.kirilldergunov.server.dto.ChangePasswordDTO;
import com.kirilldergunov.server.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordDTO dto,
            Authentication authentication) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        // проверяем старый пароль
        if (!passwordEncoder.matches(
                dto.getOldPassword(),
                user.getPassword())) {

            return ResponseEntity
                    .badRequest()
                    .body("Старый пароль неверный");
        }

        // сохраняем новый
        user.setPassword(
                passwordEncoder.encode(dto.getNewPassword()));

        userRepository.save(user);

        return ResponseEntity.ok("Пароль изменён");
    }

    @PutMapping("/change-username")
    public ResponseEntity<?> changeUsername(
            @RequestBody String newUsername,
            Authentication authentication) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        user.setUsername(newUsername);
        userRepository.save(user);

        return ResponseEntity.ok("Логин изменён");
    }
}