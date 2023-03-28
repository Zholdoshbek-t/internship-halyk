package com.halyk.study.salecard.mappers;

import com.halyk.study.salecard.dto.Objects;
import com.halyk.study.salecard.dto.Response;
import com.halyk.study.salecard.dto.enums.Status;
import org.springframework.stereotype.Component;

@Component
public class ResponseMapper {

    public static Response response(String message, Status status, Object data) {
        Response response = new Response();

        response.setMessage(message);
        response.setStatus(status);
        response.setData(data);

        return response;
    }

    public static Response response(String message, Status status, Objects dataList) {
        Response response = new Response();

        response.setMessage(message);
        response.setStatus(status);
        response.setDataList(dataList);

        return response;
    }
}
