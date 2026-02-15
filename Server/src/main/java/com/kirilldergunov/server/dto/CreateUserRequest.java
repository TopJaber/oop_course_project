package com.kirilldergunov.server.dto;

public class CreateUserRequest {

    private String username;
    private String role;

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}