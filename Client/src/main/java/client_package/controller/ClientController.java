package client_package.controller;

import client_package.api.AuthContext;
import client_package.api.ClientApi;
import client_package.model.ClientDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientController {
    private static final String BASE_URL =
            "http://localhost:8080/api/clients";

    private final ClientApi clientApi = new ClientApi();
    private final ObjectMapper mapper = new ObjectMapper();

    public void createClient(ClientDTO dto) {
        try {
            String json = mapper.writeValueAsString(dto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Authorization", AuthContext.getAuthHeader()) // ← ВАЖНО
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =
                    HttpClient.newHttpClient()
                            .send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("STATUS: " + response.statusCode());
            System.out.println("BODY: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Получение списка всех клиентов
    public List<ClientDTO> getAllClients() {
        return clientApi.getAllClients();
    }

    // Редактирование клиента
    public void updateClient(ClientDTO dto) {
        try {
            String json = mapper.writeValueAsString(dto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + dto.getId()))
                    .header("Authorization", AuthContext.getAuthHeader()) // ← ВАЖНО
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =
                    HttpClient.newHttpClient()
                            .send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("STATUS: " + response.statusCode());
            System.out.println("BODY: " + response.body());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Ошибка при обновлении клиента",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Удаление клиента по ID
    public void deleteClient(Long clientId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + clientId))
                    .header("Authorization", AuthContext.getAuthHeader()) // ← ВАЖНО
                    .header("Content-Type", "application/json")
                    .DELETE()
                    .build();

            HttpResponse<String> response =
                    HttpClient.newHttpClient()
                            .send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("STATUS: " + response.statusCode());
            System.out.println("BODY: " + response.body());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Ошибка при удалении клиента",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
