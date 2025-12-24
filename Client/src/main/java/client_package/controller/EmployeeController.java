package client_package.controller;

import client_package.model.EmployeeDTO;
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

public class EmployeeController {
    private static final String BASE_URL =
            "http://localhost:8080/employees";

    private final ObjectMapper mapper = new ObjectMapper();

    public void createEmployee(EmployeeDTO dto) {
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
                    "Ошибка при сохранении работника",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Получение списка всех сотрудников
    public List<EmployeeDTO> getAllEmployees() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/employees"))
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            JsonNode root = mapper.readTree(response.body());
            JsonNode employeesNode = root.path("_embedded").path("employees");

            if (employeesNode.isMissingNode() || !employeesNode.isArray()) {
                employeesNode = root;
            }

            List<EmployeeDTO> employees = new ArrayList<>();
            for (JsonNode empNode : employeesNode) {
                EmployeeDTO emp = mapper.treeToValue(empNode, EmployeeDTO.class);

                String href = empNode.path("_links").path("self").path("href").asText();
                if (!href.isEmpty()) {
                    String[] parts = href.split("/");
                    emp.setId(Long.parseLong(parts[parts.length - 1]));
                }

                employees.add(emp);
            }

            return employees;

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Ошибка при получении списка работников",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return Collections.emptyList();
        }
    }

    // Редактирование сотрудника
    public void updateEmployee(EmployeeDTO dto) {
        try {
            String json = mapper.writeValueAsString(dto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + dto.getId()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

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
                    .DELETE()
                    .build();

            HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Ошибка при удалении работника",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
