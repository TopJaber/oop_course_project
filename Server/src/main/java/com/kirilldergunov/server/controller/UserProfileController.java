package com.kirilldergunov.server.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;

@RestController
public class UserProfileController {

    @GetMapping("/api/me")
    public Map<String, Object> me(Authentication auth) {

        Map<String, Object> result = new HashMap<>();

        result.put("username", auth.getName());

        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        result.put("roles", roles);

        return result;
    }
}