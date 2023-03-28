package com.halyk.study.finalboss.services.impl;

import com.halyk.study.finalboss.entities.Chat;
import com.halyk.study.finalboss.repositories.ChatRepository;
import com.halyk.study.finalboss.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatImpl implements ChatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatImpl.class);

    private final ChatRepository chatRepository;


    @Override
    public void createChat(Long chatId) {
        Optional<Chat> chatOptional = chatRepository.findByChatId(chatId);

        if (chatOptional.isEmpty()) {
            Chat chat = Chat.builder()
                    .chatId(chatId)
                    .isActive(true)
                    .build();

            chatRepository.save(chat);

            LOGGER.info("New chat was created.");
        } else if(!chatOptional.get().getIsActive()) {
            chatOptional.get().setIsActive(true);

            chatRepository.save(chatOptional.get());

            LOGGER.info("Existed chat is activated.");
        } else {
            LOGGER.info("Chat already exists.");
        }
    }

    @Override
    public void deactivateChat(Long chatId) {
        Optional<Chat> chatOptional = chatRepository.findByChatId(chatId);

        if(chatOptional.isPresent()) {
            chatOptional.get().setIsActive(false);

            chatRepository.save(chatOptional.get());

            LOGGER.info("Existed chat is deactivated.");
        } else {
            LOGGER.info("Chat was not found.");
        }
    }

    @Override
    public Chat getChatByChatId(Long chatId) {
        Optional<Chat> chatOptional = chatRepository.findByChatId(chatId);

        if(chatOptional.isPresent()) {
            return chatOptional.get();
        } else {
            Chat chat = Chat.builder()
                    .chatId(chatId)
                    .isActive(true)
                    .build();

            chatRepository.save(chat);

            LOGGER.info("Chat was not found. New chat was created.");

            return chat;
        }
    }
}
