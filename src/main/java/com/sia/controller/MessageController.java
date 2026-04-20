package com.sia.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.sia.dto.MessageDTO;
import com.sia.service.MessageService;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Validated
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public MessageDTO send(@Valid @RequestBody MessageDTO dto) {
        return messageService.sendMessage(dto);
    }

    @GetMapping("/chat")
    public List<MessageDTO> getChat(@RequestParam @Positive Integer firstUserId,
                                    @RequestParam @Positive Integer secondUserId) {
        return messageService.getChat(firstUserId, secondUserId);
    }

    @GetMapping("/chat/by-ad")
    public List<MessageDTO> getChatByAdAndUsers(@RequestParam @Positive Integer adId,
                                                @RequestParam @Positive Integer firstUserId,
                                                @RequestParam @Positive Integer secondUserId){
        return messageService.getChatByAdAndUsers(adId, firstUserId, secondUserId);
    }

    @GetMapping("/ad/{adId}")
    public List<MessageDTO> getByAd(@PathVariable @Positive Integer adId) {
        return messageService.getMessagesByAd(adId);
    }

    @GetMapping("/user/{userId}")
    public List<MessageDTO> getUserMessages(@PathVariable @Positive Integer userId) {
        return messageService.getUserMessages(userId);
    }
}
