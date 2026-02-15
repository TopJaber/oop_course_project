package client_package.api;

import client_package.model.UserProfile;
import client_package.model.UserProfileDTO;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class AuthApi {

    private static final String PROFILE_URL =
            "http://localhost:8080/api/me";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static UserProfile login(String username, String password) {
        try {
            AuthContext.setCredentials(username, password);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(PROFILE_URL))
                    .header("Authorization", AuthContext.getAuthHeader())
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 401) {
                throw new RuntimeException("Неверный логин или пароль");
            }

            JsonNode root = mapper.readTree(response.body());

            String name = root.get("username").asText();

            List<String> roles = new ArrayList<>();
            root.get("roles").forEach(r -> roles.add(r.asText()));

            // 🔥 ВОТ ЭТОГО НЕ ХВАТАЛО
            AuthContext.authenticate(
                    username,
                    password,
                    roles.isEmpty() ? null : roles.get(0),
                    false
            );

            return new UserProfile(name, roles);

        } catch (Exception e) {
            AuthContext.clear();
            throw new RuntimeException(e.getMessage());
        }
    }
}