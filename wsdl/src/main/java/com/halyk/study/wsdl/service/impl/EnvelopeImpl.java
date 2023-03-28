package com.halyk.study.wsdl.service.impl;

import com.halyk.study.wsdl.dto.RestResponse;
import com.halyk.study.wsdl.dto.request.NumberToWordsRequest;
import com.halyk.study.wsdl.dto.response.NumberToWordsResponse;
import com.halyk.study.wsdl.mapper.EnvelopeMapper;
import com.halyk.study.wsdl.service.EnvelopeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class EnvelopeImpl implements EnvelopeService {

    private final EnvelopeMapper envelopeMapper;

    private final WebClient webClient;

    @Override
    public RestResponse getNumberToWordsResponse(Integer num) {
        NumberToWordsRequest numberToWordsRequest = NumberToWordsRequest.builder()
                .ubiNum(num)
                .build();

        String response = webClient.post()
                .uri("https://www.dataaccess.com/webservicesserver/NumberConversion.wso")
                .headers(httpHeaders -> {
                    httpHeaders.setContentType(MediaType.TEXT_XML);
                })
                .bodyValue(envelopeMapper.getXml(numberToWordsRequest))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        RestResponse n = envelopeMapper.getNumberToWordsResponse(response);

        return n;
    }
}
