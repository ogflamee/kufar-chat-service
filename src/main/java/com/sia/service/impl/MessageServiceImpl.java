package com.sia.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.sia.dto.MessageDTO;
import com.sia.entity.Message;
import com.sia.mapper.MessageMapper;
import com.sia.repository.MessageRepository;
import com.sia.service.MessageService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Реализация сервиса сообщений.
 * содержит бизнес-логику работы с сообщениями,
 * включая валидацию и взаимодйствие с БД.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Override
    @Transactional
    public MessageDTO sendMessage(MessageDTO dto) {
        log.info("sending message from {} to {}", dto.getSenderId(), dto.getReceiverId());

        Message message = messageMapper.toEntity(dto);
        message.setCreatedAt(LocalDateTime.now());

        Message saved = messageRepository.save(message);

        log.info("message send with id: {}", saved.getId());

        return messageMapper.toDTO(saved);
    }

    @Override
    public List<MessageDTO> getChat(Integer firstUserId, Integer secondUserId) {
        log.info("fetching chat between {} and {}", firstUserId, secondUserId);

        return messageRepository.findChat(firstUserId, secondUserId)
                .stream()
                .map(messageMapper::toDTO)
                .toList();
    }

    @Override
    public List<MessageDTO> getChatByAdAndUsers(Integer adId, Integer firstUserId, Integer secondUserId) {
        log.info("fetching chat for ad {} between {} and {}:", adId, firstUserId, secondUserId);
        return messageRepository.findChatByAdAndUsers(adId, firstUserId, secondUserId)
                .stream()
                .map(messageMapper::toDTO)
                .toList();
    }

    @Override
    public List<MessageDTO> getMessagesByAd(Integer adId) {
        log.info("fetching messages for ad: {}", adId);

        return messageRepository.findByAd(adId)
                .stream()
                .map(messageMapper::toDTO)
                .toList();
    }

    @Override
    public List<MessageDTO> getUserMessages(Integer userId) {
        log.info("fetching messages for user: {}", userId);

        return messageRepository.findBySenderOrReceiver(userId, userId)
                .stream()
                .map(messageMapper::toDTO)
                .toList();
    }
}
