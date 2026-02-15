package com.kirilldergunov.server.service;

import com.kirilldergunov.server.dao.RoleRepository;
import com.kirilldergunov.server.dao.UserRepository;
import com.kirilldergunov.server.entity.Role;
import com.kirilldergunov.server.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean hasAnyUsers() {
        return userRepository.count() > 0;
    }

    public void createAdmin(String username, String rawPassword) {

        if (hasAnyUsers()) {
            throw new IllegalStateException("Admin already exists");
        }

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setPasswordChanged(true);
        user.getRoles().add(adminRole);

        userRepository.save(user);
    }
}