package client_package.api;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import javax.swing.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UserApi {

    public static void changePassword(String oldPass, String newPass) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode json = mapper.createObjectNode();
            json.put("oldPassword", oldPass);
            json.put("newPassword", newPass);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/account/change-password"))
                    .header("Authorization", AuthContext.getAuthHeader())
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();

            HttpResponse<String> response =
                    HttpClient.newHttpClient()
                            .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                AuthContext.setCredentials(
                        AuthContext.getUsername(),
                        newPass
                );

                JOptionPane.showMessageDialog(null,
                        "Пароль успешно изменён");
            } else {
                JOptionPane.showMessageDialog(null,
                        "Ошибка: " + response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}