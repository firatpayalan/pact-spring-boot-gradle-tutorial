package com.example.pact;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit5.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@PactBroker(host = "localhost",
        port = "8081",
        scheme = "http")
@Provider("sensor_provider")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class PactProviderTest {

    @Autowired
    private SensorRepository sensorRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setupTestTarget(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port, "/"));
    }

    @BeforeAll
    static void enablePublishingPact() {
        System.setProperty("pact.verifier.publishResults", "true");
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("Two sensors exist")
    public void it_should_get_two_sensor() {
        sensorRepository.save(sensor("Sensor in the kitchen"));
        sensorRepository.save(sensor("Sensor in the bedroom"));
    }

    @State("create a sensor")
    public void it_should_create_a_sensor() {
    }



    private Sensor sensor(String name) {
        Sensor sensor = new Sensor();
        sensor.setName(name);
        return sensor;
    }
}