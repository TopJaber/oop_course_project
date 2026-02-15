package client_package.model;

import java.util.List;

public class UserProfileDTO {

    private String username;
    private List<String> roles;
    private boolean passwordChanged;

    public UserProfileDTO(String username, List<String> roles, boolean passwordChanged) {
        this.username = username;
        this.roles = roles;
        this.passwordChanged = passwordChanged;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean isPasswordChanged() {
        return passwordChanged;
    }

    public void setPasswordChanged(boolean passwordChanged) {
        this.passwordChanged = passwordChanged;
    }
}