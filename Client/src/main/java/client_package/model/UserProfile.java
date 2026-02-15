package client_package.model;

import java.util.List;

public class UserProfile {

    private final String username;
    private final List<String> roles;
    private boolean passwordChanged;

    public UserProfile(String username, List<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public boolean hasRole(String role) {
        return roles.contains(role);
    }

    public boolean isPasswordChanged() {
        return passwordChanged;
    }
}