package com.example.pact;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.jayway.jsonpath.JsonPath;
import io.pactfoundation.consumer.dsl.LambdaDsl;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.entity.ContentType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.Charset.defaultCharset;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest
@ExtendWith({PactConsumerTestExt.class})
@PactTestFor(providerName = "sensor_provider", port = "8090")
public class PactConsumerTest {

    @Autowired
    private SensorFacade sensorFacade;

    @Pact(consumer = "sensor_management")
    public RequestResponsePact sensors(PactDslWithProvider builder) throws IOException {
        final Map responseHeaders = defaultHeaders();

        return builder
                .given("Two sensors exist")
                .uponReceiving("List of sensors")
                    .path("/api/sensors")
                    .method("GET")
                .willRespondWith()
                    .status(OK.value())
                    .headers(responseHeaders)
                    .body(loadSensors())
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "sensors")
    void it_should_get_sensor() {
        Sensor[] sensors = sensorFacade.getSensors().getBody();

        assertThat(sensors).isNotEmpty();

        Sensor kitchenSensor = sensors[0];
        assertThat(kitchenSensor.getId()).isEqualTo(1);
        assertThat(kitchenSensor.getName()).isEqualTo("Sensor in the kitchen");

        Sensor bedroomSensor = sensors[1];
        assertThat(bedroomSensor.getId()).isEqualTo(2);
        assertThat(bedroomSensor.getName()).isEqualTo("Sensor in the bedroom");
    }

    @Pact(consumer="sensor_management")
    public RequestResponsePact createSensor(PactDslWithProvider builder){
        final Map requestHeaders = defaultHeaders();
        final Map responseHeaders = defaultHeaders();

        final String namePattern = "^[A-Za-z ]+$";

        return builder
                .given("create a sensor")
                .uponReceiving("created sensors given from client")
                    .method("POST")
                    .headers(requestHeaders)
                    .path("/api/sensors")
                    .body(LambdaDsl.newJsonBody((object) -> {
                        object.stringMatcher("name", namePattern, "Smoke Sensor");
                    }).build())
                .willRespondWith()
                    .headers(responseHeaders)
                    .status(201)
                    .body(LambdaDsl.newJsonBody((object) -> {
                        object.booleanType("isSuccess", true);
                        object.array("sensors",a->a.stringMatcher(namePattern,"Smoke Sensor"));
                    }).build())
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "createSensor")
    void it_should_create_sensor(MockServer mockServer) throws IOException {
        HttpResponse httpResponse = Request.Post(mockServer.getUrl() + "/api/sensors")
                .bodyString("{\"name\":\"Smoke Sensor\"}",ContentType.APPLICATION_JSON)
                .execute()
                .returnResponse();
        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(201);
        assertThat(JsonPath.read(httpResponse.getEntity().getContent(), "$.isSuccess").toString()).isEqualTo("true");
        assertThat(JsonPath.read(httpResponse.getEntity().getContent(), "$.sensors").toString()).contains("Smoke Sensor");
    }


    private Map defaultHeaders() {
        final Map headers = new HashMap<>();
        headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return headers;
    }

    private String loadSensors() throws IOException {
        return StreamUtils.copyToString(new ClassPathResource("sensors.json").getInputStream(), defaultCharset());
    }
}
