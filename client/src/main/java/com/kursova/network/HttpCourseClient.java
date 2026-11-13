package com.kursova.network;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kursova.model.Enrollment;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpCourseClient {
    private static final String BASE_URL = "http://localhost:8080/server/api/enrollments";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Enrollment> getEnrollments() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Помилка отримання даних: " + response.body());
        }

        return mapper.readValue(response.body(), new TypeReference<List<Enrollment>>() {});
    }

    public void changeStatus(int id, String status) throws Exception {
        String formData = "action=changeStatus&id=" + id + "&status=" + status;
        sendPost(formData);
    }

    public void updateFactors(int id, int f1, int f2, int f3) throws Exception {
        String formData = "action=updateFactors&id=" + id + "&f1=" + f1 + "&f2=" + f2 + "&f3=" + f3;
        sendPost(formData);
    }

    private void sendPost(String formData) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formData, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            if (response.body().contains("error")) {
                // Витягуємо повідомлення про помилку з JSON, який сформував наш Servlet
                throw new Exception(mapper.readTree(response.body()).get("error").asText());
            }
            throw new Exception("Помилка сервера: Status Code " + response.statusCode());
        }
    }
}
