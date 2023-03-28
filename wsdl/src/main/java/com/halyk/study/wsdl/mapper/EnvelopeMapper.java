package com.halyk.study.wsdl.mapper;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.halyk.study.wsdl.dto.RestResponse;
import com.halyk.study.wsdl.dto.request.BodyRequest;
import com.halyk.study.wsdl.dto.request.EnvelopeRequest;
import com.halyk.study.wsdl.dto.request.NumberToWordsRequest;
import com.halyk.study.wsdl.dto.response.EnvelopeResponse;
import com.halyk.study.wsdl.dto.response.NumberToWordsResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnvelopeMapper {

    private final XmlMapper xmlMapper;

    @SneakyThrows
    public String getXml(NumberToWordsRequest numberToWordsRequest) {
        EnvelopeRequest envelopeRequest = EnvelopeRequest.builder()
                .bodyRequest(
                        BodyRequest.builder()
                                .numberToWordsRequest(numberToWordsRequest)
                                .build())

                .build();

        return xmlMapper.writeValueAsString(envelopeRequest);
    }

    @SneakyThrows
    public RestResponse getNumberToWordsResponse(String response) {
        EnvelopeResponse envelopeResponse = xmlMapper.readValue(response, EnvelopeResponse.class);

        return RestResponse.builder()
                .message("Success")
                .body(envelopeResponse.getBody().getNumberToWords())
                .build();
    }
}
