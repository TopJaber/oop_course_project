package client_package.api;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class AuthContext {

    private static String username;
    private static String password;
    private static String role;
    private static boolean passwordChanged;

    private AuthContext() {}

    // Используется ДО логина (попытка)
    public static void setCredentials(String u, String p) {
        username = u;
        password = p;
    }

    // Используется ПОСЛЕ успешного логина
    public static void authenticate(
            String u,
            String p,
            String roleFromServer,
            boolean pwdChanged
    ) {
        username = u;
        password = p;
        role = roleFromServer;
        passwordChanged = pwdChanged;
    }

    public static void clear() {
        username = null;
        password = null;
        role = null;
        passwordChanged = false;
    }

    public static boolean isAuthenticated() {
        return username != null && password != null;
    }

    public static String getAuthHeader() {
        if (!isAuthenticated()) {
            throw new IllegalStateException("Пользователь не авторизован");
        }

        String token = username + ":" + password;
        String encoded = Base64.getEncoder()
                .encodeToString(token.getBytes(StandardCharsets.UTF_8));

        return "Basic " + encoded;
    }

    public static String getUsername() {
        return username;
    }

    public static String getRole() {
        return role;
    }

    public static boolean isPasswordChanged() {
        return passwordChanged;
    }
}