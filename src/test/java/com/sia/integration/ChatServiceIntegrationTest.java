package com.sia.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sia.dto.MessageDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ChatServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void sendMessage_shouldSave() throws Exception {
        MessageDTO request = MessageDTO.builder()
                .senderId(1)
                .receiverId(2)
                .adId(1)
                .text("Hello")
                .build();

        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.text").value("Hello"));
    }

    @Test
    @Sql({"/db/migration_test/cleanup.sql", "/db/migration_test/V3__test_data.sql"})
    void getByAd_shouldReturnMessages() throws Exception {
        mockMvc.perform(get("/api/messages/ad/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].adId").value(1));
    }

    @Test
    @Sql({"/db/migration_test/cleanup.sql", "/db/migration_test/V3__test_data.sql"})
    void getChat_shouldReturnMessages() throws Exception {
        mockMvc.perform(get("/api/messages/chat/by-ad")
                        .param("adId", "1")
                        .param("firstUserId", "100")
                        .param("secondUserId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].senderId").value(100));
    }

    @Test
    void sendMessage_shouldReturn400() throws Exception {
        MessageDTO request = MessageDTO.builder()
                .senderId(0)
                .text("")
                .build();

        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql({"/db/migration_test/cleanup.sql", "/db/migration_test/V3__test_data.sql"})
    void getChatByAdAndUsers_shouldReturnMessages() throws Exception {
        mockMvc.perform(get("/api/messages/chat/by-ad")
                .param("adId", "1")
                .param("firstUserId", "100")
                .param("secondUserId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].adId").value(1));
    }
}