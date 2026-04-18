package com.sia.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sia.dto.MessageDTO;
import com.sia.exception.GlobalExceptionHandler;
import com.sia.service.MessageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
@Import(GlobalExceptionHandler.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MessageService messageService;

    private MessageDTO buildMessageDto() {
        return MessageDTO.builder()
                .id(1)
                .senderId(1)
                .receiverId(2)
                .adId(10)
                .text("Привет, товар еще в наличии?")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("POST /api/messages - отправить сообщение")
    void send_shouldReturnCreatedMessage() throws Exception {
        MessageDTO request = MessageDTO.builder()
                .senderId(1)
                .receiverId(2)
                .adId(10)
                .text("Привет, товар еще в наличии?")
                .build();

        MessageDTO response = buildMessageDto();

        when(messageService.sendMessage(any(MessageDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.senderId").value(1))
                .andExpect(jsonPath("$.receiverId").value(2))
                .andExpect(jsonPath("$.adId").value(10))
                .andExpect(jsonPath("$.text").value("Привет, товар еще в наличии?"));

        verify(messageService).sendMessage(any(MessageDTO.class));
    }

    @Test
    @DisplayName("POST /api/messages - ошибка валидации")
    void send_shouldReturnBadRequest_whenInvalidBody() throws Exception {
        MessageDTO request = MessageDTO.builder()
                .senderId(0)
                .receiverId(null)
                .adId(-1)
                .text("")
                .build();

        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.senderId").exists())
                .andExpect(jsonPath("$.errors.receiverId").exists())
                .andExpect(jsonPath("$.errors.adId").exists())
                .andExpect(jsonPath("$.errors.text").exists());
    }

    @Test
    @DisplayName("GET /api/messages/chat - олучить чат двух пользователей")
    void getChat_shouldReturnMessages() throws Exception {
        when(messageService.getChat(1, 2)).thenReturn(List.of(buildMessageDto()));

        mockMvc.perform(get("/api/messages/chat")
                .param("firstUserId", "1")
                .param("secondUserId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].text").value("Привет, товар еще в наличии?"));

        verify(messageService).getChat(1, 2);
    }

    @Test
    @DisplayName("GET /api/messages/ad/{adId} - получить сообщения по объявлению")
    void getByAd_shouldReturnMessages() throws Exception {
        when(messageService.getMessagesByAd(10)).thenReturn(List.of(buildMessageDto()));

        mockMvc.perform(get("/api/messages/ad/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].adId").value(10));

        verify(messageService).getMessagesByAd(10);
    }

    @Test
    @DisplayName("GET /api/messages/user/{userId} - получить сообщения пользователя")
    void getUserMessages_shouldReturnMessages() throws Exception {
        when(messageService.getUserMessages(1)).thenReturn(List.of(buildMessageDto()));

        mockMvc.perform(get("/api/messages/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].senderId").value(1));

        verify(messageService).getUserMessages(1);
    }

    @Test
    @DisplayName("GET /api/messages/chat - невалидные параметры")
    void getChat_shouldReturnBadRequest_whenParamsInvalid() throws Exception {
        mockMvc.perform(get("/api/messages/chat")
                        .param("firstUserId", "0")
                        .param("secondUserId", "-1"))
                .andExpect(status().isBadRequest());
    }
}