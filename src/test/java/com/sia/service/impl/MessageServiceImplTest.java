package com.sia.service.impl;

import com.sia.dto.MessageDTO;
import com.sia.entity.Message;
import com.sia.mapper.MessageMapper;
import com.sia.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MessageMapper messageMapper;

    @InjectMocks
    private MessageServiceImpl messageService;

    @Test
    void sendMessage_success() {
        MessageDTO dto = new MessageDTO();
        dto.setSenderId(1);
        dto.setReceiverId(2);
        dto.setAdId(10);
        dto.setText("hello");

        Message message = new Message();
        Message saved = new Message();
        saved.setId(1);

        MessageDTO response = new MessageDTO();
        response.setId(1);
        response.setSenderId(1);
        response.setReceiverId(2);
        response.setAdId(10);
        response.setText("hello");

        when(messageMapper.toEntity(dto)).thenReturn(message);
        when(messageRepository.save(message)).thenReturn(saved);
        when(messageMapper.toDTO(saved)).thenReturn(response);

        MessageDTO result = messageService.sendMessage(dto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("hello", result.getText());
        assertNotNull(message.getCreatedAt());

        verify(messageMapper).toEntity(dto);
        verify(messageRepository).save(message);
        verify(messageMapper).toDTO(saved);
    }

    @Test
    void getChat_success() {
        Integer firstUserId = 1;
        Integer secondUserId = 2;

        Message message1 = new Message();
        message1.setId(1);
        message1.setCreatedAt(LocalDateTime.now());

        Message message2 = new Message();
        message2.setId(2);
        message2.setCreatedAt(LocalDateTime.now());

        MessageDTO dto1 = new MessageDTO();
        dto1.setId(1);

        MessageDTO dto2 = new MessageDTO();
        dto2.setId(2);

        when(messageRepository.findChat(firstUserId, secondUserId)).thenReturn(List.of(message1, message2));
        when(messageMapper.toDTO(message1)).thenReturn(dto1);
        when(messageMapper.toDTO(message2)).thenReturn(dto2);

        List<MessageDTO> result = messageService.getChat(firstUserId, secondUserId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());

        verify(messageRepository).findChat(firstUserId, secondUserId);
        verify(messageMapper).toDTO(message1);
        verify(messageMapper).toDTO(message2);
    }

    @Test
    void getMessagesByAd_success() {
        Integer adId = 10;

        Message message = new Message();
        message.setId(1);

        MessageDTO dto = new MessageDTO();
        dto.setId(1);

        when(messageRepository.findByAd(adId)).thenReturn(List.of(message));
        when(messageMapper.toDTO(message)).thenReturn(dto);

        List<MessageDTO> result = messageService.getMessagesByAd(adId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());

        verify(messageRepository).findByAd(adId);
        verify(messageMapper).
                toDTO(message);
    }

    @Test
    void getUserMessages_success() {
        Integer userId = 1;

        Message message1 = new Message();
        message1.setId(1);

        Message message2 = new Message();
        message2.setId(2);

        MessageDTO dto1 = new MessageDTO();
        dto1.setId(1);

        MessageDTO dto2 = new MessageDTO();
        dto2.setId(2);

        when(messageRepository.findBySenderOrReceiver(userId, userId)).thenReturn(List.of(message1, message2));
        when(messageMapper.toDTO(message1)).thenReturn(dto1);
        when(messageMapper.toDTO(message2)).thenReturn(dto2);

        List<MessageDTO> result = messageService.getUserMessages(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());

        verify(messageRepository).findBySenderOrReceiver(userId, userId);
        verify(messageMapper).toDTO(message1);
        verify(messageMapper).toDTO(message2);
    }
}