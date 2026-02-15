package com.kirilldergunov.server.config;

import com.kirilldergunov.server.dao.RoleRepository;
import com.kirilldergunov.server.dao.UserRepository;
import com.kirilldergunov.server.entity.Role;
import com.kirilldergunov.server.entity.User;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        Role adminRole = createIfNotExists("ROLE_ADMIN");
        createIfNotExists("ROLE_MANAGER");
        createIfNotExists("ROLE_EMPLOYEE");
    }

    private Role createIfNotExists(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));
    }
}