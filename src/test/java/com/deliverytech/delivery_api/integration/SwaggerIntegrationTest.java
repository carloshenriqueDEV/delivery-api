// package com.deliverytech.delivery_api.integration;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.web.client.TestRestTemplate;
// import org.springframework.boot.test.web.server.LocalServerPort;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.test.context.ActiveProfiles;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertTrue;


// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @ActiveProfiles("test")
// public class SwaggerIntegrationTest {

//     @LocalServerPort
//     private int port;

//     @Autowired
//     private TestRestTemplate restTemplate;

//     private String url(String path) {
//         return "http://localhost:" + port + path;
//     }

//     @Test
//     public void testSwaggerUIAccessible() {
//         ResponseEntity<String> response = restTemplate.getForEntity(url("/swagger-ui/index.html"), String.class);

//         assertEquals(HttpStatus.OK, response.getStatusCode(), "Swagger UI deveria estar acessível");
//         assertTrue(response.getBody().toLowerCase().contains("swagger"), "Deveria conter Swagger na UI");
//     }

//     @Test
//     public void testApiDocsAccessible() {
//         ResponseEntity<String> response = restTemplate.getForEntity(url("/v3/api-docs"), String.class);

//         assertEquals(HttpStatus.OK, response.getStatusCode(), "OpenAPI docs deveriam estar acessíveis");
//         assertTrue(response.getBody().contains("\"openapi\""), "Deveria ser um documento OpenAPI");
//         assertTrue(response.getBody().contains("DeliveryTech"), "Deve conter o nome da API na spec");
//     }

//     @Test
//     public void testApiDocsContainsExpectedEndpoints() {
//         ResponseEntity<String> response = restTemplate.getForEntity(url("/v3/api-docs"), String.class);
//         String body = response.getBody();

//         assertTrue(body.contains("/api/restaurantes"), "Endpoint Restaurantes deve existir");
//         assertTrue(body.contains("/api/produtos"), "Endpoint Produtos deve existir");
//         assertTrue(body.contains("/api/pedidos"), "Endpoint Pedidos deve existir");
//         assertTrue(body.contains("/api/auth"), "Endpoint Auth deve existir");
//     }
// }
