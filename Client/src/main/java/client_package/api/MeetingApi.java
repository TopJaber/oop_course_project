package client_package.api;

import client_package.model.EmployeeDTO;
import client_package.model.MeetingDTO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeetingApi {
    private static final String BASE_URL = "http://localhost:8080/api/meetings";

    private final ObjectMapper mapper = new ObjectMapper();

    public List<MeetingDTO> getAllMeetings() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/meetings"))
                    .header("Authorization", AuthContext.getAuthHeader())
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Ошибка: " + response.body());
            }

            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(response.body());
            JsonNode meetingsNode = root.path("_embedded").path("meetings");

            if (!meetingsNode.isArray()) {
                throw new RuntimeException("Некорректный ответ сервера");
            }

            List<MeetingDTO> meetings = new ArrayList<>();

            for (JsonNode meetingNode : meetingsNode) {

                MeetingDTO meeting =
                        mapper.treeToValue(meetingNode, MeetingDTO.class);

                String href = meetingNode
                        .path("_links")
                        .path("self")
                        .path("href")
                        .asText();

                if (!href.isEmpty()) {
                    meeting.setId(
                            Long.parseLong(
                                    href.substring(href.lastIndexOf('/') + 1)
                            )
                    );
                }

                String clientLink = meetingNode.path("_links").path("client").path("href").asText(null);
                if (clientLink != null) {
                    HttpRequest cReq = HttpRequest.newBuilder()
                            .uri(URI.create(clientLink))
                            .header("Authorization", AuthContext.getAuthHeader())
                            .GET()
                            .build();
                    HttpResponse<String> cResp = HttpClient.newHttpClient()
                            .send(cReq, HttpResponse.BodyHandlers.ofString());
                    JsonNode cNode = mapper.readTree(cResp.body());

                    String cSelf = cNode.path("_links").path("self").path("href").asText();
                    meeting.setClientId(Long.parseLong(cSelf.substring(cSelf.lastIndexOf('/') + 1)));
                    meeting.setClientFio(cNode.path("fio").asText());
                }

                String empLink = meetingNode.path("_links").path("employee").path("href").asText(null);
                if (empLink != null) {
                    HttpRequest eReq = HttpRequest.newBuilder()
                            .uri(URI.create(empLink))
                            .header("Authorization", AuthContext.getAuthHeader())
                            .GET()
                            .build();
                    HttpResponse<String> eResp = HttpClient.newHttpClient()
                            .send(eReq, HttpResponse.BodyHandlers.ofString());
                    JsonNode eNode = mapper.readTree(eResp.body());

                    String eSelf = eNode.path("_links").path("self").path("href").asText();
                    meeting.setEmployeeId(Long.parseLong(eSelf.substring(eSelf.lastIndexOf('/') + 1)));
                    meeting.setEmployeeFio(eNode.path("fio").asText());
                }

                meetings.add(meeting);
            }

            return meetings;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
