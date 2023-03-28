package com.halyk.study.finalboss.services.impl;

import com.halyk.study.finalboss.dtos.Request;
import com.halyk.study.finalboss.dtos.Response;
import com.halyk.study.finalboss.dtos.enums.Status;
import com.halyk.study.finalboss.entities.TelegramMessage;
import com.halyk.study.finalboss.repositories.TelegramMessageRepository;
import com.halyk.study.finalboss.services.TelegramMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TelegramMessageImpl implements TelegramMessageService {

    private final TelegramMessageRepository telegramMessageRepository;


    @Override
    public Response getMessageByName(Request request) {
        Optional<TelegramMessage> optional = telegramMessageRepository.findByName(request.getTelegramMsgName());

        if(optional.isPresent()) {
            return Response.builder()
                    .telegramMsgName(request.getTelegramMsgName())
                    .telegramMsgText(optional.get().getText())
                    .build();
        }

        return Response.builder()
                .telegramMsgName("Telegram Message " + request.getTelegramMsgName() + " was not found.")
                .telegramMsgText("Text is empty.")
                .build();
    }

    @Override
    public String getMessageByNameTg(String name) {
        Optional<TelegramMessage> optional = telegramMessageRepository.findByName(name);

        if(optional.isPresent()) {
            return optional.get().getText();
        }

        return "Telegram Message was not found by the give name.";
    }

    @Override
    public Response getHelpMessage() {
        String helpMessage = "";

        for(TelegramMessage telegramMessage: telegramMessageRepository.findAll()) {
            helpMessage += telegramMessage.getText();
            helpMessage += "\n\n";
        }

        return Response.builder()
                .telegramMsgText(helpMessage)
                .build();
    }

    @Override
    public Response createMessage(Request request) {
        request.setTelegramMsgName(request.getTelegramMsgName().trim().toUpperCase());

        Optional<TelegramMessage> optional = telegramMessageRepository.findByName(request.getTelegramMsgName());

        if(optional.isPresent()) {
            return Response.builder()
                    .message("Telegram Message with the give name already exists.")
                    .status(Status.ERROR)
                    .data(false)
                    .build();
        }

        TelegramMessage telegramMessage = TelegramMessage.builder()
                .name(request.getTelegramMsgName())
                .text(request.getTelegramMsgText())
                .build();

        telegramMessageRepository.save(telegramMessage);

        return Response.builder()
                .message("Telegram Message was saved.")
                .status(Status.SUCCESS)
                .data(true)
                .build();
    }

    @Override
    public Response updateMessageText(Request request) {
        request.setTelegramMsgName(request.getTelegramMsgName().trim().toUpperCase());

        Optional<TelegramMessage> optional = telegramMessageRepository.findByName(request.getTelegramMsgName());

        if(optional.isEmpty()) {
            return Response.builder()
                    .message("Telegram Message was not found.")
                    .status(Status.ERROR)
                    .data(false)
                    .build();
        } else if(request.getTelegramMsgText().trim().isBlank()) {
            return Response.builder()
                    .message("Telegram Message Text was not found in Request.")
                    .status(Status.ERROR)
                    .data(false)
                    .build();
        }

        optional.get().setText(request.getTelegramMsgText());

        telegramMessageRepository.save(optional.get());

        return Response.builder()
                .message("Telegram Message was updated.")
                .status(Status.SUCCESS)
                .data(true)
                .build();
    }

    @Override
    public Response deleteMessage(Request request) {
        request.setTelegramMsgName(request.getTelegramMsgName().trim().toUpperCase());

        Optional<TelegramMessage> optional = telegramMessageRepository.findByName(request.getTelegramMsgName());

        if(optional.isEmpty()) {
            return Response.builder()
                    .message("Telegram Message was not found.")
                    .status(Status.ERROR)
                    .data(false)
                    .build();
        }

        telegramMessageRepository.delete(optional.get());

        return Response.builder()
                .message("Telegram Message was deleted.")
                .status(Status.SUCCESS)
                .data(true)
                .build();
    }
}
