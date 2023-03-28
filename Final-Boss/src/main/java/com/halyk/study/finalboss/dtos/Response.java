package com.halyk.study.finalboss.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.halyk.study.finalboss.dtos.enums.Status;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {

    private String message;

    private Object data;

    private Status status;

    private String httpMethodName;

    private String serviceName;

    private String urlAddress;

    private int statusCode;

    private String localDateTime;

    private String telegramMsgName;

    private String telegramMsgText;

    private List<Response> errorMessageResponses;
}
