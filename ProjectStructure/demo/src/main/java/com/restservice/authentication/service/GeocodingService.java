package com.restservice.authentication.service;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Service
public class GeocodingService {

    @Value("${tomtom.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public GeocodingResult geocode(String address) throws IOException {
        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
        String url = String.format("https://api.tomtom.com/search/2/geocode/%s.json?key=%s", encodedAddress, apiKey);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                JsonNode rootNode = objectMapper.readTree(jsonResponse);
                JsonNode resultsNode = rootNode.path("results");

                if (resultsNode.isArray() && resultsNode.size() > 0) {
                    JsonNode positionNode = resultsNode.get(0).path("position");
                    double lat = positionNode.path("lat").asDouble();
                    double lon = positionNode.path("lon").asDouble();
                    return new GeocodingResult(lat, lon);
                } else {
                    throw new RuntimeException("No results found for the given address");
                }
            }
        }
    }

    public static class GeocodingResult {
        private final double lat;
        private final double lon;

        public GeocodingResult(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }
    }
}
