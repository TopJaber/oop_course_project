package client_package.api;

import client_package.model.EmployeeDTO;
import client_package.model.MeetingDTO;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MeetingApi {
    private static final String BASE_URL = "http://localhost:8080/meetings";

    private final ObjectMapper mapper = new ObjectMapper();

    public List<MeetingDTO> getAllMeetings() {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            JsonNode root = mapper.readTree(is);

            JsonNode content = root.get("_embedded").get("meetings");

            return mapper.readValue(
                    content.toString(),
                    new TypeReference<List<MeetingDTO>>() {}
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
