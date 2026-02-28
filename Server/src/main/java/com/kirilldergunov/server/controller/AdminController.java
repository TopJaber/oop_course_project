package com.kirilldergunov.server.controller;

import com.kirilldergunov.server.dao.RoleRepository;
import com.kirilldergunov.server.dao.UserRepository;
import com.kirilldergunov.server.dto.CreateUserRequest;
import com.kirilldergunov.server.entity.Role;
import com.kirilldergunov.server.entity.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public void createUser(@RequestBody CreateUserRequest req) {

        User user = new User();
        user.setUsername(req.getUsername());

        user.setPassword(passwordEncoder.encode("12345"));
        user.setPasswordChanged(false);

        Role role = roleRepository.findByName(req.getRole()).orElseThrow();
        user.getRoles().add(role);

        userRepository.save(user);
    }
}