package com.sia.service;

import com.sia.dto.MessageDTO;

import java.util.List;

public interface MessageService {

    MessageDTO sendMessage(MessageDTO dto);

    List<MessageDTO> getChat(Integer firstUserId, Integer secondUserId);

    List<MessageDTO> getMessagesByAd(Integer adId);

    List<MessageDTO> getUserMessages(Integer userId);
}
