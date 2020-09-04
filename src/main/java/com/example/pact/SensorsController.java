package com.example.pact;

import com.example.pact.message.CreateSensorRequest;
import com.example.pact.message.CreateSensorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SensorsController {

    private final SensorRepository sensorRepository;

    public SensorsController(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @GetMapping("/api/sensors")
    public ResponseEntity<List<Sensor>> getSensors() {
        List<Sensor> sensors = sensorRepository.findAll();
        ResponseEntity<List<Sensor>> responseEntity = ResponseEntity.ok()
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(sensors);
        return responseEntity;
    }

    @PostMapping("/api/sensors")
    public ResponseEntity createSensor(@RequestBody CreateSensorRequest request) {
        CreateSensorResponse response = new CreateSensorResponse();
        response.setSensors(new ArrayList<>(){{add(request.getName());}});
        response.setIsSuccess(true);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(response);
    }
}
