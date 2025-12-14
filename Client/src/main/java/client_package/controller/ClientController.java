package client_package.controller;

import client_package.model.ClientDTO;
import tools.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClientController {
    private static final String BASE_URL =
            "http://localhost:8080/api/clients";

    private final ObjectMapper mapper = new ObjectMapper();

    public void createClient(ClientDTO dto) {
        try {
            String json = mapper.writeValueAsString(dto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Ошибка при сохранении клиента",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
