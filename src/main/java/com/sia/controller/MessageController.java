package com.sia.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.sia.dto.MessageDTO;
import com.sia.service.MessageService;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public MessageDTO send(@RequestBody MessageDTO dto) {
        return messageService.sendMessage(dto);
    }

    @GetMapping("/chat")
    public List<MessageDTO> getChat(@RequestParam Integer user1,@RequestParam Integer user2) {
        return messageService.getChat(user1, user2);
    }

    @GetMapping("/ad/{adId}")
    public List<MessageDTO> getByAd(@PathVariable Integer adId) {
        return messageService.getMessagesByAd(adId);
    }

    @GetMapping("/user/{userId}")
    public List<MessageDTO> getUserMessages(@PathVariable Integer userId) {
        return messageService.getUserMessages(userId);
    }
}
