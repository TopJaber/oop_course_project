package com.kirilldergunov.server.controller;

import com.kirilldergunov.server.dao.RoleRepository;
import com.kirilldergunov.server.dao.UserRepository;
import com.kirilldergunov.server.dto.RegisterRequest;
import com.kirilldergunov.server.entity.Role;
import com.kirilldergunov.server.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest req) {

        if (userRepository.count() > 0) {
            return ResponseEntity
                    .badRequest()
                    .body("Администратор уже создан");
        }

        User admin = new User();
        admin.setUsername(req.username);
        admin.setPassword(passwordEncoder.encode(req.password));
        admin.setPasswordChanged(true);

        Role adminRole = roleRepository
                .findByName("ROLE_ADMIN")
                .orElseThrow();

        admin.getRoles().add(adminRole);

        userRepository.save(admin);

        return ResponseEntity.ok("Администратор создан");
    }
}