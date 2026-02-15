package client_package.api;

import client_package.model.ClientDTO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientApi {

    private static final String BASE_URL = "http://localhost:8080/api/clients";
    private final ObjectMapper mapper = new ObjectMapper();

    public List<ClientDTO> getAllClients() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/clients"))
                    .header("Authorization", AuthContext.getAuthHeader())
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(response.body());
            JsonNode clientsNode = root.path("_embedded").path("clients");

            if (!clientsNode.isArray()) {
                throw new RuntimeException("Некорректный ответ сервера");
            }

            List<ClientDTO> clients = new ArrayList<>();
            for (JsonNode clientNode : clientsNode) {
                ClientDTO client = mapper.treeToValue(clientNode, ClientDTO.class);

                String href = clientNode.path("_links").path("self").path("href").asText();
                if (!href.isEmpty()) {
                    client.setId(
                            Long.parseLong(href.substring(href.lastIndexOf('/') + 1))
                    );
                }

                clients.add(client);
            }

            return clients;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
