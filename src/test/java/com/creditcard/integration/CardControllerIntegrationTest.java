package com.creditcard.integration;

import com.CreditCardApiApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureTestMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = CreditCardApiApplication.class)
@AutoConfigureTestMockMvc
@Testcontainers
@Transactional
class CardControllerIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("creditcard_test")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @BeforeEach
    void setUp() throws Exception {
        // Login to get JWT token
        String loginRequest = objectMapper.writeValueAsString(
            Map.of("username", "admin", "password", "admin123")
        );

        String response = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, String> loginResponse = objectMapper.readValue(response, Map.class);
        jwtToken = "Bearer " + loginResponse.get("token");
    }

    @Test
    void shouldCreateCardSuccessfully() throws Exception {
        String request = objectMapper.writeValueAsString(
            Map.of("cardNumber", "4532015112830366")
        );

        mockMvc.perform(post("/cards")
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void shouldFindCardByNumber() throws Exception {
        // First create a card
        String createRequest = objectMapper.writeValueAsString(
            Map.of("cardNumber", "4532015112830366")
        );

        mockMvc.perform(post("/cards")
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequest))
                .andExpect(status().isCreated());

        // Then find it
        mockMvc.perform(get("/cards/4532015112830366")
                .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void shouldReturn404ForNonExistentCard() throws Exception {
        mockMvc.perform(get("/cards/1234567890123456")
                .header("Authorization", jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn401WithoutToken() throws Exception {
        String request = objectMapper.writeValueAsString(
            Map.of("cardNumber", "4532015112830366")
        );

        mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn400ForInvalidCardNumber() throws Exception {
        String request = objectMapper.writeValueAsString(
            Map.of("cardNumber", "invalid")
        );

        mockMvc.perform(post("/cards")
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest());
    }
}