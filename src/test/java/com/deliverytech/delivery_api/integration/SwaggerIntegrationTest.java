package com.deliverytech.delivery_api.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SwaggerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testSwaggerUIAccessible() {
        String url = "http://localhost:" + port + "/swagger-ui.html";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().toLowerCase().contains("swagger"));
    }

    @Test
    public void testApiDocsAccessible() {
        String url = "http://localhost:" + port + "/api-docs";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("openapi"));
        assertTrue(response.getBody().contains("DeliveryTech API"));
    }

    @Test
    public void testApiDocsContainsExpectedEndpoints() {
        String url = "http://localhost:" + port + "/api-docs";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        String body = response.getBody();
        assertTrue(body.contains("/api/restaurantes"));
        assertTrue(body.contains("/api/produtos"));
        assertTrue(body.contains("/api/pedidos"));
        assertTrue(body.contains("/api/auth"));
    }
}
