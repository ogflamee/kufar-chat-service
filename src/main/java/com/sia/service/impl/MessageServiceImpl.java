package com.sia.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sia.dto.MessageDTO;
import com.sia.entity.Message;
import com.sia.mapper.MessageMapper;
import com.sia.repository.MessageRepository;
import com.sia.service.MessageService;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Override
    public MessageDTO sendMessage(MessageDTO dto) {
        Message message = messageMapper.toEntity(dto);
        return messageMapper.toDTO(messageRepository.save(message));
    }

    @Override
    public List<MessageDTO> getChat(Integer user1, Integer user2) {
        return messageRepository.findChat(user1, user2)
                .stream()
                .map(messageMapper::toDTO)
                .toList();
    }

    @Override
    public List<MessageDTO> getMessagesByAd(Integer adId) {
        return messageRepository.findByAd(adId)
                .stream()
                .map(messageMapper::toDTO)
                .toList();
    }

    @Override
    public List<MessageDTO> getUserMessages(Integer userId) {
        return messageRepository.findBySenderOrReceiver(userId, userId)
                .stream()
                .map(messageMapper::toDTO)
                .toList();
    }
}
