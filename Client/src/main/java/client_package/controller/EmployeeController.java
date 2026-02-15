package client_package.controller;

import client_package.api.AuthContext;
import client_package.api.ClientApi;
import client_package.api.EmployeeApi;
import client_package.model.EmployeeDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.swing.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmployeeController {
    private static final String BASE_URL =
            "http://localhost:8080/api/employees";

    private final EmployeeApi employeeApi = new EmployeeApi();
    private final ObjectMapper mapper = new ObjectMapper();

    public void createEmployee(EmployeeDTO dto) {
        try {
            String json = mapper.writeValueAsString(dto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Authorization", AuthContext.getAuthHeader())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =
                    HttpClient.newHttpClient()
                            .send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("STATUS: " + response.statusCode());
            System.out.println("BODY: " + response.body());

            if (response.statusCode() != 201) {
                throw new RuntimeException(response.body());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Ошибка при сохранении работника",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Получение списка всех сотрудников
    public List<EmployeeDTO> getAllEmployees() {
        return employeeApi.getAllEmployees();
    }

    // Редактирование сотрудника
    public void updateEmployee(EmployeeDTO dto) {
        try {
            String json = mapper.writeValueAsString(dto);
            System.out.println(json);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + dto.getId()))
                    .header("Authorization", AuthContext.getAuthHeader())
                    .header("Content-Type", "application/json")
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =
                    HttpClient.newHttpClient()
                            .send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("STATUS: " + response.statusCode());

            if (response.statusCode() != 200 &&
                    response.statusCode() != 204) {
                throw new RuntimeException(response.body());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Ошибка при обновлении работника",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Удаление сотрудника по ID
    public void deleteEmployee(Long employeeId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + employeeId))
                    .header("Authorization", AuthContext.getAuthHeader())
                    .DELETE()
                    .build();

            HttpResponse<String> response =
                    HttpClient.newHttpClient()
                            .send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("STATUS: " + response.statusCode());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Ошибка при удалении работника",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        try {
            ObjectNode json = new ObjectMapper().createObjectNode();
            json.put("oldPassword", oldPassword);
            json.put("newPassword", newPassword);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/users/change-password"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();

            HttpResponse<Void> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.discarding());

            return response.statusCode() == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
