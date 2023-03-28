package com.halyk.study.finalboss.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Request {

    private Long apiServiceId;

    private String apiServiceName;

    private Long urlId;

    private String address;

    private String httpMethodName;

    private String newHttpMethodName;

    private String telegramMsgName;

    private String telegramMsgText;
}
