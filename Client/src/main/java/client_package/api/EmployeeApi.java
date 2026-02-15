package client_package.api;

import client_package.model.ClientDTO;
import client_package.model.EmployeeDTO;
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

public class EmployeeApi {
    private static final String BASE_URL = "http://localhost:8080/api/employees";

    private final ObjectMapper mapper = new ObjectMapper();

    public List<EmployeeDTO> getAllEmployees() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/employees"))
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
            JsonNode employeesNode = root.path("_embedded").path("employees");

            if (!employeesNode.isArray()) {
                throw new RuntimeException("Некорректный ответ сервера");
            }

            List<EmployeeDTO> employees = new ArrayList<>();

            for (JsonNode employeeNode : employeesNode) {

                EmployeeDTO employee =
                        mapper.treeToValue(employeeNode, EmployeeDTO.class);

                String href = employeeNode
                        .path("_links")
                        .path("self")
                        .path("href")
                        .asText();

                if (!href.isEmpty()) {
                    employee.setId(
                            Long.parseLong(
                                    href.substring(href.lastIndexOf('/') + 1)
                            )
                    );
                }

                employees.add(employee);
            }

            return employees;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
