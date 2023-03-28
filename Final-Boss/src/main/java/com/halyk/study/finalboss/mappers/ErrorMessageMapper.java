package com.halyk.study.finalboss.mappers;

import com.halyk.study.finalboss.dtos.Response;
import com.halyk.study.finalboss.entities.ErrorMessage;
import com.halyk.study.finalboss.utils.TelegramBot;
import org.springframework.stereotype.Component;

@Component
public class ErrorMessageMapper {

    public static Response createErrorMessageResponse(ErrorMessage errorMessage) {
        return Response.builder()
                .urlAddress(errorMessage.getUrl().getAddress())
                .serviceName(errorMessage.getUrl().getApiService().getName())
                .statusCode(errorMessage.getCode())
                .localDateTime(TelegramBot.DATE_TIME_FORMATTER.format(errorMessage.getCreatedAt()))
                .build();
    }
}
