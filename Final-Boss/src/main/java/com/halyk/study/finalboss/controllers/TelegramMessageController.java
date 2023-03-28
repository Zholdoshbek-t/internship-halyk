package com.halyk.study.finalboss.controllers;

import com.halyk.study.finalboss.dtos.Request;
import com.halyk.study.finalboss.dtos.Response;
import com.halyk.study.finalboss.services.TelegramMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TelegramMessageController {

    private final TelegramMessageService telegramMessageService;


    @GetMapping("/get-message-by-name")
    public ResponseEntity<Response> getMessageByName(@RequestBody Request request) {
        return ResponseEntity.ok(telegramMessageService.getMessageByName(request));
    }

    @PostMapping("/create-message")
    public ResponseEntity<Response> createMessage(@RequestBody Request request) {
        return ResponseEntity.ok(telegramMessageService.createMessage(request));
    }

    @PostMapping("/update-message")
    public ResponseEntity<Response> updateMessageText(@RequestBody Request request) {
        return ResponseEntity.ok(telegramMessageService.updateMessageText(request));
    }

    @PostMapping("/delete-message")
    public ResponseEntity<Response> deleteMessage(@RequestBody Request request) {
        return ResponseEntity.ok(telegramMessageService.deleteMessage(request));
    }
}
