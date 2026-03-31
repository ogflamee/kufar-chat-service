package com.sia.service;

import com.sia.dto.MessageDTO;

import java.util.List;

public interface MessageService {

    MessageDTO sendMessage(MessageDTO dto);

    List<MessageDTO> getChat(Integer user1, Integer user2);

    List<MessageDTO> getMessagesByAd(Integer adId);

    List<MessageDTO> getUserMessages(Integer userId);
}
