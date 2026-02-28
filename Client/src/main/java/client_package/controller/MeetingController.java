package client_package.controller;

import client_package.api.AuthContext;
import client_package.api.MeetingApi;
import client_package.model.MeetingDTO;
import client_package.model.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.swing.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class MeetingController {
    private static final String BASE_URL = "http://localhost:8080/api/meetings";
    private final ObjectMapper mapper = new ObjectMapper();

    private final MeetingApi meetingApi = new MeetingApi();
    private final ClientController clientController;
    private final EmployeeController employeeController;

    public MeetingController(ClientController clientController, EmployeeController employeeController) {
        this.clientController = clientController;
        this.employeeController = employeeController;
    }

    // Получение всех встреч
    public List<MeetingDTO> getAllMeetings() {
        return meetingApi.getAllMeetings();
    }

    // Создание встречи
    public void createMeeting(MeetingDTO dto) {
        try {
            if (dto.getStatus() == null) dto.setStatus(Status.SCHEDULED);

            ObjectNode node = mapper.createObjectNode();
            node.put("datetime", dto.getDatetime().toString());
            node.put("place", dto.getPlace());
            node.put("comment", dto.getComment());
            node.put("status", dto.getStatus().name());

            ObjectNode clientNode = mapper.createObjectNode();
            clientNode.put("id", dto.getClientId());
            node.set("client", clientNode);

            ObjectNode employeeNode = mapper.createObjectNode();
            employeeNode.put("id", dto.getEmployeeId());
            node.set("employee", employeeNode);

            String json = mapper.writeValueAsString(node);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Authorization", AuthContext.getAuthHeader())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new RuntimeException("Meeting not created: " + response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при создании встречи", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Обновление встречи
    public void updateMeeting(MeetingDTO dto) {
        try {
            if (dto.getStatus() == null) dto.setStatus(Status.SCHEDULED);

            ObjectNode node = mapper.createObjectNode();
            node.put("datetime", dto.getDatetime().toString());
            node.put("place", dto.getPlace());
            node.put("comment", dto.getComment());
            node.put("status", dto.getStatus().name());

            ObjectNode clientNode = mapper.createObjectNode();
            clientNode.put("id", dto.getClientId());
            node.set("client", clientNode);

            ObjectNode employeeNode = mapper.createObjectNode();
            employeeNode.put("id", dto.getEmployeeId());
            node.set("employee", employeeNode);

            String json = mapper.writeValueAsString(node);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + dto.getId()))
                    .header("Authorization", AuthContext.getAuthHeader())
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new RuntimeException("Meeting not updated: " + response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при обновлении встречи", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Удаление встречи
    public void deleteMeeting(Long meetingId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Authorization", AuthContext.getAuthHeader())
                    .header("Content-Type", "application/json")
                    .uri(URI.create(BASE_URL + "/" + meetingId))
                    .DELETE()
                    .build();

            HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при удалении встречи", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
