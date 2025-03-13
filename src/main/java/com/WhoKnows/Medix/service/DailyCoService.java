package com.WhoKnows.Medix.service;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@Service
public class DailyCoService {
    private static final String DAILY_CO_API_URL = "https://api.daily.co/v1/rooms";
    private static final String API_KEY = "ec6b13956f59a319b80208d35c18dc1a50473dbfd4144884be9f01f5d8cf3707"; // Replace with your API Key

    public String createMeeting(String doctorId, String patientId) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Request Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + API_KEY);

            // Generate unique room name
            String roomName = "doc-" + doctorId + "-pat-" + patientId + "-" + System.currentTimeMillis();

            // Request Body (JSON)
            JSONObject requestBody = new JSONObject();
            requestBody.put("name", roomName);
            requestBody.put("privacy", "public"); // Options: 'public' or 'private'
            requestBody.put("properties", new JSONObject()
                    .put("exp", System.currentTimeMillis() / 1000 + 3600) // Expires in 1 hour
            );

            HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);
            ResponseEntity<String> response = restTemplate.exchange(DAILY_CO_API_URL, HttpMethod.POST, request, String.class);

            JSONObject jsonResponse = new JSONObject(response.getBody());
            return jsonResponse.getString("url"); // Return generated meeting URL

        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return null in case of failure
        }
    }
}
