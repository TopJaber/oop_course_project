package client_package.controller;

import client_package.model.ClientDTO;
import client_package.model.EmployeeDTO;
import client_package.model.MeetingDTO;
import client_package.model.Status;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.swing.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeetingController {
    private static final String BASE_URL = "http://localhost:8080/meetings";
    private final ObjectMapper mapper = new ObjectMapper();
    private final ClientController clientController;
    private final EmployeeController employeeController;

    public MeetingController(ClientController clientController, EmployeeController employeeController) {
        this.clientController = clientController;
        this.employeeController = employeeController;
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    // Получение всех встреч
    public List<MeetingDTO> getAllMeetings() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode root = mapper.readTree(response.body());
            JsonNode meetingsNode = root.path("_embedded").path("meetings");
            if (meetingsNode.isMissingNode() || !meetingsNode.isArray()) {
                return Collections.emptyList();
            }

            List<MeetingDTO> meetings = new ArrayList<>();
            for (JsonNode node : meetingsNode) {
                MeetingDTO dto = new MeetingDTO();
                dto.setId(node.path("id").asLong());
                dto.setDatetime(LocalDateTime.parse(node.path("datetime").asText()));
                dto.setStatus(Status.valueOf(node.path("status").asText("SCHEDULED")));
                dto.setPlace(node.path("place").asText(""));
                dto.setComment(node.path("comment").asText(""));

                // Подгружаем клиента
                String clientUrl = node.path("_links").path("client").path("href").asText(null);
                if (clientUrl != null) {
                    HttpRequest cReq = HttpRequest.newBuilder()
                            .uri(URI.create(clientUrl))
                            .GET().build();
                    HttpResponse<String> cResp = HttpClient.newHttpClient()
                            .send(cReq, HttpResponse.BodyHandlers.ofString());
                    JsonNode cNode = mapper.readTree(cResp.body());
                    dto.setClientId(cNode.path("id").asLong());
                    dto.setClientFio(cNode.path("fio").asText(""));
                }

                // Подгружаем сотрудника
                String empUrl = node.path("_links").path("employee").path("href").asText(null);
                if (empUrl != null) {
                    HttpRequest eReq = HttpRequest.newBuilder()
                            .uri(URI.create(empUrl))
                            .GET().build();
                    HttpResponse<String> eResp = HttpClient.newHttpClient()
                            .send(eReq, HttpResponse.BodyHandlers.ofString());
                    JsonNode eNode = mapper.readTree(eResp.body());
                    dto.setEmployeeId(eNode.path("id").asLong());
                    dto.setEmployeeFio(eNode.path("fio").asText(""));
                }

                meetings.add(dto);
            }
            return meetings;

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при получении списка встреч", "Error", JOptionPane.ERROR_MESSAGE);
            return Collections.emptyList();
        }
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
            ObjectNode node = mapper.createObjectNode();
            node.put("datetime", dto.getDatetime().toString());
            node.put("place", dto.getPlace());
            node.put("comment", dto.getComment());
            node.put("status", dto.getStatus() != null ? dto.getStatus().name() : Status.SCHEDULED.name());

            ObjectNode clientNode = mapper.createObjectNode();
            clientNode.put("id", dto.getClientId());
            node.set("client", clientNode);

            ObjectNode employeeNode = mapper.createObjectNode();
            employeeNode.put("id", dto.getEmployeeId());
            node.set("employee", employeeNode);

            String json = mapper.writeValueAsString(node);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + dto.getId()))
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
                    .uri(URI.create(BASE_URL + "/" + meetingId))
                    .DELETE()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new RuntimeException("Meeting not deleted: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при удалении встречи", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
