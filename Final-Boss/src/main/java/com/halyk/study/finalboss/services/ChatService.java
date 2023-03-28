package com.halyk.study.finalboss.services;

import com.halyk.study.finalboss.entities.Chat;

public interface ChatService {

    void createChat(Long chatId);

    void deactivateChat(Long chatId);

    Chat getChatByChatId(Long chatId);
}
