package com.halyk.study.finalboss.services;

import com.halyk.study.finalboss.dtos.Request;
import com.halyk.study.finalboss.dtos.Response;

public interface TelegramMessageService {

    Response getMessageByName(Request request);

    String getMessageByNameTg(String name);

    Response getHelpMessage();

    Response createMessage(Request request);

    Response updateMessageText(Request request);

    Response deleteMessage(Request request);
}
