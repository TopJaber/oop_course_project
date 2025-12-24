package client_package.api;

import client_package.model.ClientDTO;
import client_package.model.EmployeeDTO;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class EmployeeApi {
    private static final String BASE_URL = "http://localhost:8080/employees";

    private final ObjectMapper mapper = new ObjectMapper();

    public List<EmployeeDTO> getAllEmployees() {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            JsonNode root = mapper.readTree(is);

            JsonNode content = root.get("_embedded").get("employees");

            return mapper.readValue(
                    content.toString(),
                    new TypeReference<List<EmployeeDTO>>() {}
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
