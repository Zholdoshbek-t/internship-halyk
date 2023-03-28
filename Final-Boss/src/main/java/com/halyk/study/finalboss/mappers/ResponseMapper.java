package com.halyk.study.finalboss.mappers;

import com.halyk.study.finalboss.dtos.Response;
import com.halyk.study.finalboss.dtos.enums.Status;
import org.springframework.stereotype.Component;

@Component
public class ResponseMapper {

    public static Response createResponse(String message, Status status, Object data) {
        return Response.builder()
                .message(message)
                .status(status)
                .data(data)
                .build();
    }
}
