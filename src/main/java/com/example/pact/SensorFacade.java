package com.example.pact;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SensorFacade {

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<Sensor[]> getSensors() {
        final String url = "http://localhost:8090/api/sensors";
        return restTemplate.getForEntity(url, Sensor[].class);
    }
}
